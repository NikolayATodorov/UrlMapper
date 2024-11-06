import dayjs from 'dayjs/esm';

export interface IUrlMapping {
  id: number;
  originalUrl?: string | null;
  ttl?: number | null;
  creationTimestamp?: dayjs.Dayjs | null;
}

export type NewUrlMapping = Omit<IUrlMapping, 'id'> & { id: null };
