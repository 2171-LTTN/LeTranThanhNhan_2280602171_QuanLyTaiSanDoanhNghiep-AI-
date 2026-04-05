import axios from 'axios';

const API_BASE_URL = '/api';
const LANGUAGE_STORAGE_KEY = 'language';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    const lang = localStorage.getItem(LANGUAGE_STORAGE_KEY);
    config.headers['Accept-Language'] = lang === 'en' ? 'en' : 'vi';
    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

/** Prefer first field error from validation responses (ApiResponse data.errors). */
export function getAxiosErrorMessage(error: unknown, fallback: string): string {
  type ApiErrBody = { message?: string; data?: { errors?: Record<string, string> } };
  const ax = error as { response?: { data?: ApiErrBody } };
  const errs = ax.response?.data?.data?.errors;
  if (errs && typeof errs === 'object') {
    const first = Object.values(errs).find((v) => v != null && String(v).trim() !== '');
    if (first != null) return String(first);
  }
  const msg = ax.response?.data?.message;
  if (msg) return msg;
  return fallback;
}

export default api;
