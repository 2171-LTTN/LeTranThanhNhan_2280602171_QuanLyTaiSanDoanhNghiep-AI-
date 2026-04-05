import { useState, useEffect, type FormEvent } from 'react';
import { useTranslation } from 'react-i18next';
import { requestService } from '../services/requestService';
import { assetService } from '../services/assetService';
import { assetCategoryMatchesRequest } from '../constants/assetRequestCategories';
import type { AssetRequest, Asset } from '../types';

const STATUS_COLORS: Record<string, string> = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  APPROVED: 'bg-green-100 text-green-700',
  REJECTED: 'bg-red-100 text-red-700',
  CANCELLED: 'bg-gray-100 text-gray-700',
};

interface ModalProps {
  title: string;
  onClose: () => void;
  children: React.ReactNode;
}

function Modal({ title, onClose, children }: ModalProps) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/50" onClick={onClose}></div>
      <div className="relative bg-white rounded-xl shadow-xl w-full max-w-lg mx-4 p-6">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-gray-900">{title}</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        {children}
      </div>
    </div>
  );
}

function formatDate(iso: string): string {
  return new Date(iso).toLocaleDateString('vi-VN', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

export default function AdminRequestsPage() {
  const { t } = useTranslation();

  const categoryLabel = (code: string) =>
    t(`assets.categories.${code.toLowerCase()}`, { defaultValue: code });
  const [requests, setRequests] = useState<AssetRequest[]>([]);
  const [assets, setAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [selectedRequest, setSelectedRequest] = useState<AssetRequest | null>(null);
  const [actionType, setActionType] = useState<'approve' | 'reject' | null>(null);
  const [reviewNote, setReviewNote] = useState('');
  const [selectedAssetId, setSelectedAssetId] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [filter, setFilter] = useState<'all' | 'pending'>('pending');

  const fetchRequests = async () => {
    try {
      setLoading(true);
      const data = await requestService.getAll(0, 100);
      const filtered = filter === 'pending'
        ? data.content.filter(r => r.status === 'PENDING')
        : data.content;
      setRequests(filtered);
    } catch {
      setError(t('errors.loadFailed'));
    } finally {
      setLoading(false);
    }
  };

  const fetchAssets = async () => {
    try {
      const data = await assetService.getAll();
      const availableAssets = data.filter(a => a.status === 'AVAILABLE');
      setAssets(availableAssets);
    } catch {
      // silently fail
    }
  };

  useEffect(() => {
    fetchRequests();
  }, [filter]);

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 3000);
  };

  const openApproveModal = (request: AssetRequest) => {
    setSelectedRequest(request);
    setActionType('approve');
    setReviewNote('');
    setSelectedAssetId('');
    fetchAssets();
  };

  const openRejectModal = (request: AssetRequest) => {
    setSelectedRequest(request);
    setActionType('reject');
    setReviewNote('');
  };

  const closeModal = () => {
    setSelectedRequest(null);
    setActionType(null);
    setReviewNote('');
    setSelectedAssetId('');
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!selectedRequest || !actionType) return;

    setSubmitting(true);
    try {
      if (actionType === 'approve') {
        if (!selectedAssetId) {
          setError(t('requests.selectAssetRequired'));
          setSubmitting(false);
          return;
        }
        await requestService.approve(selectedRequest.id, {
          reviewNote: reviewNote || undefined,
          assetId: selectedAssetId,
        });
        showSuccess(t('requests.approveSuccess'));
      } else {
        await requestService.reject(selectedRequest.id, {
          reviewNote: reviewNote || undefined,
        });
        showSuccess(t('requests.rejectSuccess'));
      }
      closeModal();
      await fetchRequests();
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message || t('errors.saveFailed');
      setError(msg);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="p-8">
        <div className="animate-pulse space-y-4">
          <div className="h-8 bg-gray-200 rounded w-48"></div>
          <div className="h-12 bg-gray-200 rounded"></div>
          {[1, 2, 3].map((i) => <div key={i} className="h-20 bg-gray-200 rounded"></div>)}
        </div>
      </div>
    );
  }

  const pendingCount = requests.filter(r => r.status === 'PENDING').length;

  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">{t('requests.adminTitle')}</h1>
          <p className="text-gray-500 mt-1">{t('requests.adminSubtitle')}</p>
        </div>
        <div className="flex gap-2">
          <button
            onClick={() => setFilter('pending')}
            className={`px-4 py-2 text-sm font-medium rounded-lg transition-colors ${
              filter === 'pending'
                ? 'bg-yellow-100 text-yellow-700'
                : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            }`}
          >
            {t('requests.pending')} ({pendingCount})
          </button>
          <button
            onClick={() => setFilter('all')}
            className={`px-4 py-2 text-sm font-medium rounded-lg transition-colors ${
              filter === 'all'
                ? 'bg-indigo-100 text-indigo-700'
                : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            }`}
          >
            {t('requests.all')}
          </button>
        </div>
      </div>

      {successMsg && (
        <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
          {successMsg}
        </div>
      )}

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
          {error}
          <button type="button" onClick={() => setError('')} className="font-medium underline shrink-0">{t('common.dismiss')}</button>
        </div>
      )}

      {/* Requests List */}
      <div className="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-100">
              <tr>
                {['requests.requester', 'requests.category', 'requests.allocatedAsset', 'requests.reason', 'requests.status', 'requests.createdAt', 'common.actions'].map((h) => (
                  <th key={h} className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{t(h)}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {requests.length === 0 ? (
                <tr>
                  <td colSpan={7} className="px-6 py-12 text-center text-gray-500">{t('requests.noRequests')}</td>
                </tr>
              ) : (
                requests.map((request) => (
                  <tr key={request.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4">
                      <div>
                        <p className="text-sm font-medium text-gray-900">{request.requestedByUserName}</p>
                        <p className="text-xs text-gray-500">{request.requestedByUserEmail}</p>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{categoryLabel(request.category)}</td>
                    <td className="px-6 py-4 text-sm text-gray-600">
                      {request.assetName || '—'}
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-600 max-w-xs truncate">{request.reason}</td>
                    <td className="px-6 py-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${STATUS_COLORS[request.status]}`}>
                        {t(`requests.status${request.status.charAt(0) + request.status.slice(1).toLowerCase()}`)}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-500">{formatDate(request.createdAt)}</td>
                    <td className="px-6 py-4 flex items-center gap-2">
                      {request.status === 'PENDING' && (
                        <>
                          <button
                            onClick={() => openApproveModal(request)}
                            className="text-xs text-green-600 hover:text-green-800 font-medium"
                          >
                            {t('requests.approve')}
                          </button>
                          <button
                            onClick={() => openRejectModal(request)}
                            className="text-xs text-red-600 hover:text-red-800 font-medium"
                          >
                            {t('requests.reject')}
                          </button>
                        </>
                      )}
                      {request.reviewNote && (
                        <p className="text-xs text-gray-500 mt-1">{request.reviewNote}</p>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Approve Modal */}
      {selectedRequest && actionType === 'approve' && (
        <Modal title={t('requests.approve')} onClose={closeModal}>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="bg-gray-50 rounded-lg p-4">
              <p className="text-sm font-medium text-gray-700">{t('requests.requestedCategory')}</p>
              <p className="text-lg font-semibold text-gray-900">{categoryLabel(selectedRequest.category)}</p>
              <p className="text-xs text-gray-500 mt-1">{t('requests.adminPickAssetHint')}</p>
            </div>
            <div className="bg-blue-50 rounded-lg p-4">
              <p className="text-sm font-medium text-gray-700">{t('requests.requestedBy')}</p>
              <p className="text-sm text-gray-900">{selectedRequest.requestedByUserName}</p>
              <p className="text-xs text-gray-500">{selectedRequest.requestedByUserEmail}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">{t('requests.assignAsset')} *</label>
              <select
                value={selectedAssetId}
                onChange={(e) => setSelectedAssetId(e.target.value)}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none bg-white"
              >
                <option value="">— {t('requests.selectAsset')} —</option>
                {assets
                  .filter((a) => assetCategoryMatchesRequest(selectedRequest.category, a.category))
                  .map((asset) => (
                    <option key={asset.id} value={asset.id}>
                      {asset.name} ({asset.serialNumber}) — {asset.category}
                    </option>
                  ))}
              </select>
              <p className="text-xs text-gray-500 mt-1">{t('requests.assignAssetHint')}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">{t('requests.reviewNote')}</label>
              <textarea
                value={reviewNote}
                onChange={(e) => setReviewNote(e.target.value)}
                rows={2}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 outline-none"
                placeholder={t('requests.reviewNotePlaceholder')}
              ></textarea>
            </div>
            <div className="flex justify-end gap-3 pt-2">
              <button type="button" onClick={closeModal} className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900">{t('common.cancel')}</button>
              <button type="submit" disabled={submitting} className="px-4 py-2 text-sm bg-green-600 hover:bg-green-700 text-white rounded-lg disabled:opacity-50">{submitting ? '...' : t('requests.approve')}</button>
            </div>
          </form>
        </Modal>
      )}

      {/* Reject Modal */}
      {selectedRequest && actionType === 'reject' && (
        <Modal title={t('requests.reject')} onClose={closeModal}>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="bg-gray-50 rounded-lg p-4">
              <p className="text-sm font-medium text-gray-700">{t('requests.requestedCategory')}</p>
              <p className="text-lg font-semibold text-gray-900">{categoryLabel(selectedRequest.category)}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">{t('requests.reason')}</label>
              <textarea
                value={selectedRequest.reason}
                disabled
                rows={2}
                className="w-full px-3 py-2 border border-gray-200 rounded-lg bg-gray-50 text-gray-500"
              ></textarea>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">{t('requests.rejectionReason')} *</label>
              <textarea
                value={reviewNote}
                onChange={(e) => setReviewNote(e.target.value)}
                required
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-red-500 outline-none"
                placeholder={t('requests.rejectionReasonPlaceholder')}
              ></textarea>
            </div>
            <div className="flex justify-end gap-3 pt-2">
              <button type="button" onClick={closeModal} className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900">{t('common.cancel')}</button>
              <button type="submit" disabled={submitting} className="px-4 py-2 text-sm bg-red-600 hover:bg-red-700 text-white rounded-lg disabled:opacity-50">{submitting ? '...' : t('requests.reject')}</button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
