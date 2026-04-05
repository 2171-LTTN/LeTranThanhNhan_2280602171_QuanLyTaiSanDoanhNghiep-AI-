import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { assetService } from '../services/assetService';
import type { Asset, AssetHistory } from '../types';

const STATUS_COLORS: Record<string, string> = {
  AVAILABLE: 'bg-green-100 text-green-700',
  IN_USE: 'bg-blue-100 text-blue-700',
  BROKEN: 'bg-red-100 text-red-700',
};

const STATUS_LABELS: Record<string, string> = {
  AVAILABLE: 'assets.statusAvailable',
  IN_USE: 'assets.statusInUse',
  BROKEN: 'assets.statusBroken',
};

function formatTimestamp(iso: string): string {
  const date = new Date(iso);
  return date.toLocaleString('vi-VN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

export default function MyAssetsPage() {
  const { t } = useTranslation();
  const [assets, setAssets] = useState<Asset[]>([]);
  const [history, setHistory] = useState<AssetHistory[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMsg, setSuccessMsg] = useState('');
  const [activeTab, setActiveTab] = useState<'assets' | 'history'>('assets');

  const fetchData = async () => {
    try {
      setLoading(true);
      const [assetsData, historyData] = await Promise.all([
        assetService.getMyAssets(),
        assetService.getMyHistories(),
      ]);
      setAssets(assetsData);
      setHistory(historyData);
    } catch {
      setError(t('errors.loadFailed'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const showSuccess = (msg: string) => {
    setSuccessMsg(msg);
    setTimeout(() => setSuccessMsg(''), 3000);
  };

  const handleReturn = async (asset: Asset) => {
    try {
      await assetService.returnMine(asset.id);
      showSuccess(t('assets.returnSuccess'));
      await fetchData();
    } catch (err: unknown) {
      const msg = (err as { response?: { data?: { message?: string } } })?.response?.data?.message || t('errors.saveFailed');
      setError(msg);
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
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-gray-900">{t('myAssets.title')}</h1>
        <p className="text-gray-500 mt-1">{t('myAssets.subtitle')}</p>
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

      {/* Tabs */}
      <div className="flex gap-4 mb-6">
        <button
          onClick={() => setActiveTab('assets')}
          className={`px-4 py-2 text-sm font-medium rounded-lg transition-colors ${
            activeTab === 'assets'
              ? 'bg-indigo-100 text-indigo-700'
              : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
          }`}
        >
          {t('myAssets.myAssets')} ({assets.length})
        </button>
        <button
          onClick={() => setActiveTab('history')}
          className={`px-4 py-2 text-sm font-medium rounded-lg transition-colors ${
            activeTab === 'history'
              ? 'bg-indigo-100 text-indigo-700'
              : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
          }`}
        >
          {t('myAssets.history')} ({history.length})
        </button>
      </div>

      {activeTab === 'assets' ? (
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-gray-50 border-b border-gray-100">
                <tr>
                  {['assets.assetName', 'assets.category', 'assets.status', 'assets.serialNumber', 'assets.location', 'common.actions'].map((h) => (
                    <th key={h} className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">{t(h)}</th>
                  ))}
                </tr>
              </thead>
              <tbody className="divide-y divide-gray-100">
                {assets.length === 0 ? (
                  <tr>
                    <td colSpan={6} className="px-6 py-12 text-center text-gray-500">{t('myAssets.noAssets')}</td>
                  </tr>
                ) : (
                  assets.map((asset) => (
                    <tr key={asset.id} className="hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4 text-sm font-medium text-gray-900">{asset.name}</td>
                      <td className="px-6 py-4 text-sm text-gray-600">{asset.category}</td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${STATUS_COLORS[asset.status] || 'bg-gray-100 text-gray-700'}`}>
                          {t(STATUS_LABELS[asset.status] || asset.status)}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-600">{asset.serialNumber || '—'}</td>
                      <td className="px-6 py-4 text-sm text-gray-600">{asset.location || '—'}</td>
                      <td className="px-6 py-4">
                        {asset.status === 'IN_USE' && (
                          <button
                            onClick={() => handleReturn(asset)}
                            className="text-xs text-blue-600 hover:text-blue-800 font-medium"
                          >
                            {t('assets.return')}
                          </button>
                        )}
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
          {history.length === 0 ? (
            <p className="text-center text-gray-500 py-8">{t('assets.noHistory')}</p>
          ) : (
            <div className="space-y-3">
              {history.map((item) => (
                <div key={item.id} className="flex items-center gap-3 p-3 bg-gray-50 rounded-lg">
                  <div className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-medium ${
                    item.action === 'ASSIGNED' ? 'bg-blue-100 text-blue-700' :
                    item.action === 'RETURNED' ? 'bg-green-100 text-green-700' :
                    'bg-gray-100 text-gray-700'
                  }`}>
                    {item.action === 'ASSIGNED' ? 'A' : item.action === 'RETURNED' ? 'R' : 'U'}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-900 truncate">{item.assetName}</p>
                    <p className="text-xs text-gray-500">
                      {item.action === 'ASSIGNED' ? t('assets.assigned') : item.action === 'RETURNED' ? t('assets.returned') : item.action} — {item.details}
                    </p>
                  </div>
                  <span className="text-xs text-gray-400">{formatTimestamp(item.timestamp)}</span>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}
