import dayjs from 'dayjs/esm';

import { IUrlMapping, NewUrlMapping } from './url-mapping.model';

export const sampleWithRequiredData: IUrlMapping = {
  id: 10817,
  originalUrl: 'selfishly',
  creationTimestamp: dayjs('2024-10-30T17:06'),
};

export const sampleWithPartialData: IUrlMapping = {
  id: 32743,
  originalUrl: 'annually joint',
  creationTimestamp: dayjs('2024-10-30T16:03'),
};

export const sampleWithFullData: IUrlMapping = {
  id: 18194,
  originalUrl: 'nervous yippee instead',
  ttl: 8199,
  creationTimestamp: dayjs('2024-10-30T21:39'),
};

export const sampleWithNewData: NewUrlMapping = {
  originalUrl: 'meh throughout times',
  creationTimestamp: dayjs('2024-10-31T06:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
