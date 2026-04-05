import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import { useNavigate } from 'react-router-dom';
import { dashboardService } from '../services/dashboardService';
import { historyService } from '../services/historyService';
import { assetService } from '../services/assetService';
import { useAuth } from '../context/AuthContext';
import type { DashboardStats, AssetHistory, Asset } from '../types';

function StatCard({ label, value, color }: { label: string; value: number; color: string }) {
  return (
    <div className="bg-white rounded-xl p-6 border border-gray-100 shadow-sm">
      <p className="text-sm text-gray-500 mb-1">{label}</p>
      <p className={`text-3xl font-bold ${color}`}>{value}</p>
    </div>
  );
}

function formatTimestamp(iso: string): string {
  const date = new Date(iso);
  return date.toLocaleString('vi-VN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

export default function DashboardPage() {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const { isAdmin } = useAuth();
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [recentActivity, setRecentActivity] = useState<AssetHistory[]>([]);
  const [myAssets, setMyAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        if (isAdmin) {
          const [statsData, historyData] = await Promise.all([
            dashboardService.getStats(),
            historyService.getAll(),
          ]);
          setStats(statsData);
          setRecentActivity(historyData.slice(0, 10));
        } else {
          const [assetsData, historyData] = await Promise.all([
            assetService.getMyAssets(),
            assetService.getMyHistories(),
          ]);
          setMyAssets(assetsData);
          setRecentActivity(historyData.slice(0, 10));
        }
      } catch {
        setError(t('errors.loadFailed'));
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [isAdmin]);

  if (loading) {
    return (
      <div className="p-8">
        <div className="animate-pulse space-y-6">
          <div className="h-8 bg-gray-200 rounded w-48"></div>
          <div className="grid grid-cols-4 gap-4">
            {[1, 2, 3, 4].map((i) => (
              <div key={i} className="h-32 bg-gray-200 rounded-xl"></div>
            ))}
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-8">
        <div className="bg-red-50 border border-red-200 rounded-xl p-6 text-center">
          <p className="text-red-700">{error}</p>
          <button
            onClick={() => window.location.reload()}
            className="mt-3 text-sm text-red-600 hover:text-red-800 font-medium"
          >
            {t('errors.tryAgain')}
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-900">{t('dashboard.title')}</h1>
        <p className="text-gray-500 mt-1">{t('dashboard.assetDistribution')}</p>
      </div>

      {isAdmin ? (
        <>
          {/* ADMIN VIEW: Full Dashboard */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
            <StatCard label={t('dashboard.totalAssets')} value={stats?.totalAssets ?? 0} color="text-gray-900" />
            <StatCard label={t('dashboard.available')} value={stats?.availableAssets ?? 0} color="text-green-600" />
            <StatCard label={t('dashboard.inUse')} value={stats?.inUseAssets ?? 0} color="text-blue-600" />
            <StatCard label={t('dashboard.broken')} value={stats?.brokenAssets ?? 0} color="text-red-600" />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div className="bg-white rounded-xl p-6 border border-gray-100 shadow-sm">
              <h2 className="text-base font-semibold text-gray-900 mb-4">{t('dashboard.totalUsers')}</h2>
              <div className="space-y-3">
                <div className="flex justify-between items-center">
                  <span className="text-sm text-gray-600">{t('dashboard.totalUsers')}</span>
                  <span className="text-sm font-semibold text-gray-900">{stats?.totalUsers ?? 0}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-sm text-gray-600">{t('users.admin')}</span>
                  <span className="text-sm font-semibold text-indigo-600">{stats?.adminUsers ?? 0}</span>
                </div>
                <div className="flex justify-between items-center">
                  <span className="text-sm text-gray-600">{t('users.user')}</span>
                  <span className="text-sm font-semibold text-gray-900">{stats?.staffUsers ?? 0}</span>
                </div>
              </div>
            </div>

            <div className="lg:col-span-2 bg-white rounded-xl p-6 border border-gray-100 shadow-sm">
              <h2 className="text-base font-semibold text-gray-900 mb-4">{t('dashboard.recentActivity')}</h2>
              {recentActivity.length === 0 ? (
                <p className="text-sm text-gray-500 text-center py-8">{t('assets.noHistory')}</p>
              ) : (
                <div className="space-y-3">
                  {recentActivity.map((item) => (
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
                          {item.userName} &mdash; {item.action.toLowerCase()}
                        </p>
                      </div>
                      <span className="text-xs text-gray-400">{formatTimestamp(item.timestamp)}</span>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </>
      ) : (
        <>
          {/* USER VIEW: My Dashboard */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <StatCard label={t('myAssets.myTotalAssets')} value={myAssets.length} color="text-gray-900" />
            <StatCard label={t('myAssets.inUse')} value={myAssets.filter(a => a.status === 'IN_USE').length} color="text-blue-600" />
            <StatCard label={t('myAssets.available')} value={myAssets.filter(a => a.status === 'AVAILABLE').length} color="text-green-600" />
          </div>

          <div className="bg-white rounded-xl p-6 border border-gray-100 shadow-sm">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-base font-semibold text-gray-900">{t('myAssets.myAssets')}</h2>
              <button
                onClick={() => navigate('/my-assets')}
                className="text-sm text-indigo-600 hover:text-indigo-800 font-medium"
              >
                {t('common.viewAll')} &rarr;
              </button>
            </div>
            {myAssets.length === 0 ? (
              <p className="text-sm text-gray-500 text-center py-8">{t('myAssets.noAssets')}</p>
            ) : (
              <div className="space-y-3">
                {myAssets.slice(0, 5).map((asset) => (
                  <div key={asset.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                    <div className="flex items-center gap-3">
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-medium ${
                        asset.status === 'IN_USE' ? 'bg-blue-100 text-blue-700' :
                        asset.status === 'AVAILABLE' ? 'bg-green-100 text-green-700' :
                        'bg-red-100 text-red-700'
                      }`}>
                        {asset.status === 'IN_USE' ? 'U' : asset.status === 'AVAILABLE' ? 'A' : 'B'}
                      </div>
                      <div>
                        <p className="text-sm font-medium text-gray-900">{asset.name}</p>
                        <p className="text-xs text-gray-500">{asset.category}</p>
                      </div>
                    </div>
                    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                      asset.status === 'IN_USE' ? 'bg-blue-100 text-blue-700' :
                      asset.status === 'AVAILABLE' ? 'bg-green-100 text-green-700' :
                      'bg-red-100 text-red-700'
                    }`}>
                      {t(asset.status === 'IN_USE' ? 'assets.statusInUse' : asset.status === 'AVAILABLE' ? 'assets.statusAvailable' : 'assets.statusBroken')}
                    </span>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="mt-6 bg-white rounded-xl p-6 border border-gray-100 shadow-sm">
            <h2 className="text-base font-semibold text-gray-900 mb-4">{t('myAssets.history')}</h2>
            {recentActivity.length === 0 ? (
              <p className="text-sm text-gray-500 text-center py-8">{t('assets.noHistory')}</p>
            ) : (
              <div className="space-y-3">
                {recentActivity.map((item) => (
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
                        {item.action === 'ASSIGNED' ? t('assets.assigned') : item.action === 'RETURNED' ? t('assets.returned') : item.action}
                      </p>
                    </div>
                    <span className="text-xs text-gray-400">{formatTimestamp(item.timestamp)}</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        </>
      )}
    </div>
  );
}
