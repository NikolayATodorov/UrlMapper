import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '9dae5ff4-72d8-472a-96f6-72fc15a1ffd8',
};

export const sampleWithPartialData: IAuthority = {
  name: 'f5dc982b-c4cd-4460-89f9-1cdb71d29fe5',
};

export const sampleWithFullData: IAuthority = {
  name: '362f5472-b947-4665-a58a-2d8d9704677b',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
