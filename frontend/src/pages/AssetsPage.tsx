import { useState, useEffect, type FormEvent } from 'react';
import { assetService } from '../services/assetService';
import { userService } from '../services/userService';
import type { Asset, User } from '../types';

const STATUS_COLORS: Record<string, string> = {
  AVAILABLE: 'bg-green-100 text-green-700',
  IN_USE: 'bg-blue-100 text-blue-700',
  BROKEN: 'bg-red-100 text-red-700',
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

export default function AssetsPage() {
  const [assets, setAssets] = useState<Asset[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [showAssignModal, setShowAssignModal] = useState<Asset | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<Asset | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [successMsg, setSuccessMsg] = useState('');

  // Form states
  const [formName, setFormName] = useState('');
  const [formCategory, setFormCategory] = useState('');
  const [formPurchaseDate, setFormPurchaseDate] = useState('');
  const [formPurchasePrice, setFormPurchasePrice] = useState('');
  const [formSerialNumber, setFormSerialNumber] = useState('');
  const [formBrand, setFormBrand] = useState('');
  const [formModel, setFormModel] = useState('');
  const [formWarrantyUntil, setFormWarrantyUntil] = useState('');
  const [formLocation, setFormLocation] = useState('');
  const [formNote, setFormNote] = useState('');
  const [formAssignUserId, setFormAssignUserId] = useState('');

  const fetchAssets = async () => {
    try {
      const data = await assetService.getAll();
      setAssets(data);
    } catch {
      setError('Failed to load assets.');
    }
  };

  const fetchUsers = async () => {
    try {
      const data = await userService.getAll();
      setUsers(data);
    } catch {
      // silently fail
    }
  };

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      await Promise.all([fetchAssets(), fetchUsers()]);
      setLoading(false);
    };
    load();
  }, []);

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 3000);
  };

  const resetForm = () => {
    setFormName('');
    setFormCategory('');
    setFormPurchaseDate('');
    setFormPurchasePrice('');
    setFormSerialNumber('');
    setFormBrand('');
    setFormModel('');
    setFormWarrantyUntil('');
    setFormLocation('');
    setFormNote('');
    setFormAssignUserId('');
  };

  const handleAdd = async (e: FormEvent) => {
    e.preventDefault();
    setSubmitting(true);
    try {
      await assetService.create({
        name: formName,
        category: formCategory,
        purchaseDate: formPurchaseDate || undefined,
        purchasePrice: formPurchasePrice ? parseFloat(formPurchasePrice) : undefined,
        serialNumber: formSerialNumber,
        brand: formBrand,
        model: formModel,
        warrantyUntil: formWarrantyUntil || undefined,
        location: formLocation,
        note: formNote || undefined,
      });
      setShowAddModal(false);
      resetForm();
      await fetchAssets();
      showSuccess('Asset created successfully.');
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Failed to create asset.';
      setError(msg);
    } finally {
      setSubmitting(false);
    }
  };

  const handleAssign = async (e: FormEvent) => {
    e.preventDefault();
    if (!showAssignModal) return;
    setSubmitting(true);
    try {
      await assetService.assign(showAssignModal.id, { userId: formAssignUserId });
      setShowAssignModal(null);
      resetForm();
      await fetchAssets();
      showSuccess('Asset assigned successfully.');
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Failed to assign asset.';
      setError(msg);
    } finally {
      setSubmitting(false);
    }
  };

  const handleReturn = async (asset: Asset) => {
    try {
      await assetService.return(asset.id);
      await fetchAssets();
      showSuccess('Asset returned successfully.');
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Failed to return asset.';
      setError(msg);
    }
  };

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setSubmitting(true);
    try {
      await assetService.delete(deleteTarget.id);
      setDeleteTarget(null);
      await fetchAssets();
      showSuccess('Asset deleted successfully.');
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message || 'Failed to delete asset.';
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
          {[1, 2, 3].map((i) => <div key={i} className="h-16 bg-gray-200 rounded"></div>)}
        </div>
      </div>
    );
  }

  return (
    <div className="p-8">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Assets</h1>
          <p className="text-gray-500 mt-1">Manage company assets</p>
        </div>
        <button
          onClick={() => setShowAddModal(true)}
          className="flex items-center gap-2 px-4 py-2.5 bg-indigo-600 hover:bg-indigo-700 text-white text-sm font-medium rounded-lg transition-colors"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Add Asset
        </button>
      </div>

      {successMsg && (
        <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
          {successMsg}
        </div>
      )}

      {error && (
        <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-700 text-sm">
          {error}
          <button onClick={() => setError('')} className="ml-2 font-medium underline">Dismiss</button>
        </div>
      )}

      <div className="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-100">
              <tr>
                {['Name', 'Category', 'Status', 'Assigned To', 'Location', 'Actions'].map((h) => (
                  <th key={h} className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{h}</th>
                ))}
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {assets.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-6 py-12 text-center text-gray-500">No assets found.</td>
                </tr>
              ) : (
                assets.map((asset) => (
                  <tr key={asset.id} className="hover:bg-gray-50 transition-colors">
                    <td className="px-6 py-4 text-sm font-medium text-gray-900">{asset.name}</td>
                    <td className="px-6 py-4 text-sm text-gray-600">{asset.category}</td>
                    <td className="px-6 py-4">
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${STATUS_COLORS[asset.status] || 'bg-gray-100 text-gray-700'}`}>
                        {asset.status.replace('_', ' ')}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-600">{asset.assignedToName || '—'}</td>
                    <td className="px-6 py-4 text-sm text-gray-600">{asset.location || '—'}</td>
                    <td className="px-6 py-4 flex items-center gap-2">
                      {asset.status === 'AVAILABLE' && (
                        <button
                          onClick={() => { setShowAssignModal(asset); fetchUsers(); }}
                          className="text-xs text-indigo-600 hover:text-indigo-800 font-medium"
                        >
                          Assign
                        </button>
                      )}
                      {asset.status === 'IN_USE' && (
                        <button
                          onClick={() => handleReturn(asset)}
                          className="text-xs text-blue-600 hover:text-blue-800 font-medium"
                        >
                          Return
                        </button>
                      )}
                      <button
                        onClick={() => setDeleteTarget(asset)}
                        className="text-xs text-red-600 hover:text-red-800 font-medium"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Add Asset Modal */}
      {showAddModal && (
        <Modal title="Add Asset" onClose={() => { setShowAddModal(false); resetForm(); }}>
          <form onSubmit={handleAdd} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Name *</label>
                <input value={formName} onChange={(e) => setFormName(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g. Dell Laptop XPS 15" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Category *</label>
                <input value={formCategory} onChange={(e) => setFormCategory(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g. Laptop" />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Serial Number *</label>
                <input value={formSerialNumber} onChange={(e) => setFormSerialNumber(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g. SN123456" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Brand *</label>
                <input value={formBrand} onChange={(e) => setFormBrand(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g. Dell" />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Model *</label>
                <input value={formModel} onChange={(e) => setFormModel(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g. XPS 15" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Location *</label>
                <input value={formLocation} onChange={(e) => setFormLocation(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g. Office Floor 1" />
              </div>
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Purchase Price (VND)</label>
                <input type="number" value={formPurchasePrice} onChange={(e) => setFormPurchasePrice(e.target.value)} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="e.g. 25000000" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Purchase Date</label>
                <input type="date" value={formPurchaseDate} onChange={(e) => setFormPurchaseDate(e.target.value)} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" />
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Warranty Until</label>
              <input type="date" value={formWarrantyUntil} onChange={(e) => setFormWarrantyUntil(e.target.value)} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Note</label>
              <textarea value={formNote} onChange={(e) => setFormNote(e.target.value)} rows={2} className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none" placeholder="Optional notes..."></textarea>
            </div>
            <div className="flex justify-end gap-3 pt-2">
              <button type="button" onClick={() => { setShowAddModal(false); resetForm(); }} className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900">Cancel</button>
              <button type="submit" disabled={submitting} className="px-4 py-2 text-sm bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg disabled:opacity-50">{submitting ? 'Creating...' : 'Create'}</button>
            </div>
          </form>
        </Modal>
      )}

      {/* Assign Modal */}
      {showAssignModal && (
        <Modal title={`Assign: ${showAssignModal.name}`} onClose={() => { setShowAssignModal(null); resetForm(); }}>
          <form onSubmit={handleAssign} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Select User *</label>
              <select value={formAssignUserId} onChange={(e) => setFormAssignUserId(e.target.value)} required className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none bg-white">
                <option value="">— Choose a user —</option>
                {users.map((u) => <option key={u.id} value={u.id}>{u.name} ({u.role})</option>)}
              </select>
            </div>
            <div className="flex justify-end gap-3 pt-2">
              <button type="button" onClick={() => { setShowAssignModal(null); resetForm(); }} className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900">Cancel</button>
              <button type="submit" disabled={submitting || !formAssignUserId} className="px-4 py-2 text-sm bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg disabled:opacity-50">{submitting ? 'Assigning...' : 'Assign'}</button>
            </div>
          </form>
        </Modal>
      )}

      {/* Delete Confirmation */}
      {deleteTarget && (
        <Modal title="Delete Asset" onClose={() => setDeleteTarget(null)}>
          <p className="text-gray-600 mb-6">
            Are you sure you want to delete <strong className="text-gray-900">{deleteTarget.name}</strong>? This action cannot be undone.
          </p>
          <div className="flex justify-end gap-3">
            <button onClick={() => setDeleteTarget(null)} className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900">Cancel</button>
            <button onClick={handleDelete} disabled={submitting} className="px-4 py-2 text-sm bg-red-600 hover:bg-red-700 text-white rounded-lg disabled:opacity-50">{submitting ? 'Deleting...' : 'Delete'}</button>
          </div>
        </Modal>
      )}
    </div>
  );
}
