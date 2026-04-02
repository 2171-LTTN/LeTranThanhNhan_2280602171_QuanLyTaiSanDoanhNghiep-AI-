import api from './api';
import type { DashboardStats } from '../types';

export const dashboardService = {
  getStats: async (): Promise<DashboardStats> => {
    const response = await api.get<{ data: DashboardStats }>('/dashboard/stats');
    return response.data.data;
  },
};
