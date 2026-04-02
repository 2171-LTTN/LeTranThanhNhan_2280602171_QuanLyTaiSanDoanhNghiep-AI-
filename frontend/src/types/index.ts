export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface UserInfo {
  id: string;
  name: string;
  email: string;
  role: string;
  department?: string;
  position?: string;
  phone?: string;
}

export interface AuthResponse {
  token: string;
  tokenType: string;
  user: UserInfo;
}

export interface User {
  id: string;
  name: string;
  email: string;
  role: string;
  department?: string;
  position?: string;
  phone?: string;
}

export interface Asset {
  id: string;
  name: string;
  category: string;
  status: 'AVAILABLE' | 'IN_USE' | 'BROKEN';
  assignedTo: string | null;
  assignedToName: string | null;
  purchaseDate: string | null;
  purchasePrice: number | null;
  serialNumber: string | null;
  brand: string | null;
  model: string | null;
  warrantyUntil: string | null;
  location: string | null;
  note: string | null;
}

export interface CreateAssetRequest {
  name: string;
  category: string;
  purchaseDate?: string;
  purchasePrice?: number;
  serialNumber: string;
  brand: string;
  model: string;
  warrantyUntil?: string;
  location: string;
  note?: string;
}

export interface UpdateAssetRequest {
  name?: string;
  category?: string;
  purchaseDate?: string;
  purchasePrice?: number;
  serialNumber?: string;
  brand?: string;
  model?: string;
  warrantyUntil?: string;
  location?: string;
  note?: string;
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
  performedBy: string;
  action: 'CREATED' | 'ASSIGNED' | 'RETURNED' | 'UPDATED' | 'DELETED';
  details: string;
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
  message?: string;
  data: T;
  timestamp?: string;
}
