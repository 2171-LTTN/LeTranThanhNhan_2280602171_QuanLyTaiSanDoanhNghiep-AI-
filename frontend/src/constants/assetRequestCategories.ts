/** Must match backend com.lttn.quanlytaisan.model.AssetRequestCategory */
export const ASSET_REQUEST_CATEGORY_CODES = [
  'LAPTOP',
  'DESKTOP',
  'MONITOR',
  'KEYBOARD',
  'MOUSE',
  'HEADPHONE',
  'PRINTER',
  'PHONE',
  'TABLET',
  'OTHER',
] as const;

export type AssetRequestCategoryCode = (typeof ASSET_REQUEST_CATEGORY_CODES)[number];

/** Same rules as backend AssetRequestCategory#matchesAssetCategory */
export function assetCategoryMatchesRequest(
  requestCode: string,
  assetCategory: string | null | undefined
): boolean {
  const ac = (assetCategory || '').trim().toLowerCase();
  const code = (requestCode || 'OTHER').toUpperCase();
  switch (code) {
    case 'LAPTOP':
      return ac.includes('laptop');
    case 'DESKTOP':
      return ac.includes('desktop') || ac.includes('máy bàn') || ac.includes('may ban');
    case 'MONITOR':
      return ac.includes('monitor') || ac.includes('màn hình') || ac.includes('man hinh');
    case 'KEYBOARD':
      return ac.includes('keyboard') || ac.includes('bàn phím') || ac.includes('ban phim');
    case 'MOUSE':
      return ac.includes('mouse') || ac.includes('chuột') || ac.includes('chuot');
    case 'HEADPHONE':
      return ac.includes('headphone') || ac.includes('tai nghe') || ac.includes('tainghe');
    case 'PRINTER':
      return ac.includes('printer') || ac.includes('máy in') || ac.includes('may in');
    case 'PHONE':
      return ac.includes('phone') || ac.includes('điện thoại') || ac.includes('dien thoai');
    case 'TABLET':
      return ac.includes('tablet') || ac.includes('máy tính bảng') || ac.includes('may tinh bang');
    case 'OTHER':
    default:
      return true;
  }
}
