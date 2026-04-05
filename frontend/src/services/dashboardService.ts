import api from './api';
import type { DashboardStats } from '../types';

const emptyStats: DashboardStats = {
  totalAssets: 0,
  availableAssets: 0,
  inUseAssets: 0,
  brokenAssets: 0,
  totalUsers: 0,
  adminUsers: 0,
  staffUsers: 0,
};

function normalizeStats(raw: unknown): DashboardStats {
  if (!raw || typeof raw !== 'object') {
    return { ...emptyStats };
  }
  const o = raw as Record<string, unknown>;
  const n = (v: unknown) => (typeof v === 'number' && !Number.isNaN(v) ? v : 0);
  return {
    totalAssets: n(o.totalAssets),
    availableAssets: n(o.availableAssets),
    inUseAssets: n(o.inUseAssets),
    brokenAssets: n(o.brokenAssets),
    totalUsers: n(o.totalUsers),
    adminUsers: n(o.adminUsers),
    staffUsers: n(o.staffUsers),
  };
}

export const dashboardService = {
  getStats: async (): Promise<DashboardStats> => {
    const response = await api.get<{ data?: DashboardStats }>('/dashboard/stats');
    return normalizeStats(response.data?.data);
  },
};
