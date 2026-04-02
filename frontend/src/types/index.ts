export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  role: string;
}

export interface AuthResponse {
  token: string;
  id: string;
  name: string;
  email: string;
  role: string;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: string;
}

export interface Asset {
  id: string;
  name: string;
  category: string;
  status: 'AVAILABLE' | 'IN_USE' | 'BROKEN';
  assignedTo: string | null;
  assignedToName: string | null;
  purchaseDate: string | null;
}

export interface CreateAssetRequest {
  name: string;
  category: string;
  status: string;
  purchaseDate?: string;
}

export interface UpdateAssetRequest {
  name?: string;
  category?: string;
  status?: string;
  purchaseDate?: string;
}

export interface CreateUserRequest {
  name: string;
  email: string;
  password: string;
  role: string;
}

export interface AssignAssetRequest {
  userId: string;
}

export interface AssetHistory {
  id: string;
  assetId: string;
  assetName: string;
  userId: string;
  userName: string;
  action: 'ASSIGNED' | 'RETURNED' | 'REPAIRED';
  timestamp: string;
}

export interface DashboardStats {
  totalAssets: number;
  availableAssets: number;
  inUseAssets: number;
  brokenAssets: number;
  totalUsers: number;
  adminUsers: number;
  staffUsers: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}
