import { cleanEntity, mapIdList } from './entity-utils';

describe('Entity utils', () => {
  describe('cleanEntity', () => {
    it('should safely handle nullish entity values', () => {
      expect(cleanEntity(null)).toBeNull();
      expect(cleanEntity(undefined)).toBeUndefined();
    });

    it('should not remove fields with an id', () => {
      const entityA = {
        a: {
          id: 5,
        },
      };
      const entityB = {
        a: {
          id: '5',
        },
      };

      expect(cleanEntity({ ...entityA })).toEqual(entityA);
      expect(cleanEntity({ ...entityB })).toEqual(entityB);
    });

    it('should remove fields with an empty id', () => {
      const entity = {
        a: {
          id: '',
        },
      };

      expect(cleanEntity({ ...entity })).toEqual({});
    });

    it('should not remove fields that are not objects', () => {
      const entity = {
        a: '',
        b: 5,
        c: [],
        d: '5',
      };

      expect(cleanEntity({ ...entity })).toEqual(entity);
    });
  });

  describe('mapIdList', () => {
    it("should map ids no matter the element's type", () => {
      const ids = ['jhipster', '', 1, { key: 'value' } as any];

      expect(mapIdList(ids)).toEqual([{ id: 'jhipster' }, { id: 1 }, { id: { key: 'value' } }]);
    });

    it('should return an empty array', () => {
      const ids = [];

      expect(mapIdList(ids)).toEqual([]);
    });

    it('should ignore nullish or empty identifiers', () => {
      const ids = ['jhipster', null, undefined, ''];

      expect(mapIdList(ids as any)).toEqual([{ id: 'jhipster' }]);
      expect(mapIdList(undefined)).toEqual([]);
      expect(mapIdList(null as any)).toEqual([]);
    });
  });
});
