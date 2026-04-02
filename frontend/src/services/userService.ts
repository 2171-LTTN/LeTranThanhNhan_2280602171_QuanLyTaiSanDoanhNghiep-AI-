import api from './api';
import type { User } from '../types';

interface PageUserResponse {
  content: User[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

export const userService = {
  getAll: async (): Promise<User[]> => {
    const response = await api.get<{ data: PageUserResponse }>('/users');
    return response.data.data.content;
  },

  getById: async (id: string): Promise<User> => {
    const response = await api.get<{ data: User }>(`/users/${id}`);
    return response.data.data;
  },
};
