import { useState, useEffect, type FormEvent } from 'react';
import { useTranslation } from 'react-i18next';
import { getAxiosErrorMessage } from '../services/api';
import { requestService } from '../services/requestService';
import { ASSET_REQUEST_CATEGORY_CODES } from '../constants/assetRequestCategories';
import type { AssetRequest } from '../types';

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

export default function MyRequestsPage() {
  const { t } = useTranslation();
  const [requests, setRequests] = useState<AssetRequest[]>([]);
  const [loading, setLoading] = useState(true);
  /** List GET failed — do not show “no requests” empty state */
  const [listLoadError, setListLoadError] = useState<string | null>(null);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  // Form state — user chỉ chọn danh mục; tên tài sản do admin chọn khi duyệt
  const [formCategory, setFormCategory] = useState('');
  const [formReason, setFormReason] = useState('');
  const [formNote, setFormNote] = useState('');

  const categoryLabel = (code: string) => {
    const key = code.toLowerCase();
    return t(`assets.categories.${key}`, { defaultValue: code });
  };

  const fetchRequests = async () => {
    try {
      setLoading(true);
      setListLoadError(null);
      const data = await requestService.getMy();
      setRequests(data);
    } catch (err: unknown) {
      setListLoadError(getAxiosErrorMessage(err, t('errors.loadFailed')));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchRequests();
  }, []);

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 3000);
  };

  const resetForm = () => {
    setFormCategory('');
    setFormReason('');
    setFormNote('');
  };

  const handleCreate = async (e: FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await requestService.create({
        category: formCategory,
        reason: formReason,
        note: formNote || undefined,
      });
      setShowCreateModal(false);
      resetForm();
      await fetchRequests();
      showSuccess(t('requests.submitSuccess'));
    } catch (err: unknown) {
      setError(getAxiosErrorMessage(err, t('errors.saveFailed')));
    } finally {
      setSubmitting(false);
    }
  };

  const handleCancel = async (id: string) => {
    try {
      await requestService.cancel(id);
      showSuccess(t('requests.cancelSuccess'));
      await fetchRequests();
    } catch (err: unknown) {
      setError(getAxiosErrorMessage(err, t('errors.saveFailed')));
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

  const pendingCount = requests.filter((r) => r.status === 'PENDING').length;
  const approvedCount = requests.filter((r) => r.status === 'APPROVED').length;
  const rejectedCount = requests.filter((r) => r.status === 'REJECTED' || r.status === 'CANCELLED').length;
  const listOk = !listLoadError;

  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">{t('requests.title')}</h1>
          <p className="text-gray-500 mt-1">{t('requests.subtitle')}</p>
        </div>
        <button
          onClick={() => {
            setError('');
            setShowCreateModal(true);
          }}
          className="flex items-center gap-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium rounded-lg transition-colors"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          {t('requests.newRequest')}
        </button>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-3 gap-4 mb-6">
        <div className="bg-white rounded-xl p-4 border border-gray-100 shadow-sm">
          <p className="text-sm text-gray-500">{t('requests.pending')}</p>
          <p className="text-2xl font-bold text-yellow-600">{listOk ? pendingCount : '—'}</p>
        </div>
        <div className="bg-white rounded-xl p-4 border border-gray-100 shadow-sm">
          <p className="text-sm text-gray-500">{t('requests.approved')}</p>
          <p className="text-2xl font-bold text-green-600">{listOk ? approvedCount : '—'}</p>
        </div>
        <div className="bg-white rounded-xl p-4 border border-gray-100 shadow-sm">
          <p className="text-sm text-gray-500">{t('requests.rejected')}</p>
          <p className="text-2xl font-bold text-red-600">{listOk ? rejectedCount : '—'}</p>
        </div>
      </div>

      {successMsg && (
        <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
          {successMsg}
        </div>
      )}

      {listLoadError && !showCreateModal && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm flex flex-wrap items-center gap-3">
          <span className="min-w-0 flex-1">{listLoadError}</span>
          <button
            type="button"
            onClick={() => void fetchRequests()}
            className="shrink-0 px-3 py-1.5 text-sm font-medium bg-white border border-red-200 rounded-lg hover:bg-red-50"
          >
            {t('common.retry')}
          </button>
        </div>
      )}

      {error && !showCreateModal && !listLoadError && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm flex flex-wrap items-center gap-2">
          <span>{error}</span>
          <button type="button" onClick={() => setError('')} className="font-medium underline shrink-0">
            {t('common.dismiss')}
          </button>
        </div>
      )}

      {/* Requests List */}
      <div className="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-100">
              <tr>
                {['requests.category', 'requests.allocatedAsset', 'requests.reason', 'requests.status', 'requests.createdAt', 'common.actions'].map((h) => (
                  <th key={h} className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{t(h)}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {listLoadError ? (
                <tr>
                  <td colSpan={6} className="px-6 py-12 text-center">
                    <p className="text-gray-600 text-sm mb-4 max-w-md mx-auto">{t('requests.listNotLoadedHint')}</p>
                    <button
                      type="button"
                      onClick={() => void fetchRequests()}
                      className="px-4 py-2 text-sm font-medium bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg"
                    >
                      {t('common.retry')}
                    </button>
                  </td>
                </tr>
              ) : requests.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-6 py-12 text-center text-gray-500">{t('requests.noRequests')}</td>
                </tr>
              ) : (
                requests.map((request) => (
                  <tr key={request.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{categoryLabel(request.category)}</td>
                    <td className="px-6 py-4 text-sm text-gray-600">
                      {request.assetName ? (
                        request.assetName
                      ) : (
                        <span className="text-gray-400 italic">{t('requests.pendingAllocation')}</span>
                      )}
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-600 max-w-xs truncate">{request.reason}</td>
                    <td className="px-6 py-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${STATUS_COLORS[request.status]}`}>
                        {t(`requests.status${request.status.charAt(0) + request.status.slice(1).toLowerCase()}`)}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-500">{formatDate(request.createdAt)}</td>
                    <td className="px-6 py-4">
                      {request.status === 'PENDING' && (
                        <button
                          onClick={() => handleCancel(request.id)}
                          className="text-xs text-red-600 hover:text-red-800 font-medium"
                        >
                          {t('requests.cancel')}
                        </button>
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

      {/* Create Request Modal */}
      {showCreateModal && (
        <Modal title={t('requests.newRequest')} onClose={() => { setShowCreateModal(false); resetForm(); setError(''); }}>
          <form onSubmit={handleCreate} className="space-y-4">
            {error && (
              <div className="p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm flex flex-wrap items-center gap-2">
                <span>{error}</span>
                <button type="button" onClick={() => setError('')} className="font-medium underline shrink-0">
                  {t('common.dismiss')}
                </button>
              </div>
            )}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">{t('requests.category')} *</label>
              <select
                value={formCategory}
                onChange={(e) => setFormCategory(e.target.value)}
                required
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none bg-white"
              >
                <option value="">— {t('requests.selectCategory')} —</option>
                {ASSET_REQUEST_CATEGORY_CODES.map((code) => (
                  <option key={code} value={code}>
                    {t(`assets.categories.${code.toLowerCase()}`, { defaultValue: code })}
                  </option>
                ))}
              </select>
              <p className="text-xs text-gray-500 mt-1">{t('requests.categoryOnlyHint')}</p>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">{t('requests.reason')} *</label>
              <textarea
                value={formReason}
                onChange={(e) => setFormReason(e.target.value)}
                required
                rows={3}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                placeholder={t('requests.reasonPlaceholder')}
              ></textarea>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">{t('requests.note')}</label>
              <textarea
                value={formNote}
                onChange={(e) => setFormNote(e.target.value)}
                rows={2}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none"
                placeholder={t('requests.notePlaceholder')}
              ></textarea>
            </div>
            <div className="flex justify-end gap-3 pt-2">
              <button
                type="button"
                onClick={() => {
                  setShowCreateModal(false);
                  resetForm();
                  setError('');
                }}
                className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900"
              >
                {t('common.cancel')}
              </button>
              <button type="submit" disabled={submitting} className="px-4 py-2 text-sm bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg disabled:opacity-50">{submitting ? '...' : t('common.create')}</button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
