import { AxiosResponse } from 'axios';
import { createContext, PropsWithChildren, useState } from 'react';

import { CACHE_TIME } from '@/constants/cache';

type CacheBody = {
  response: AxiosResponse<unknown> | AxiosResponse<unknown>[];
  expires: number;
};
type CacheMap = Map<string, CacheBody | CacheBody[]>;

export const GetCacheContext =
  createContext<
    (
      url: string,
      page?: number
    ) => AxiosResponse<unknown, unknown> | AxiosResponse<unknown, unknown>[]
  >(null);
export const AddCacheContext =
  createContext<(url: string, response: AxiosResponse<unknown>, maxAge?: number) => void>(
    null
  );
export const AddCacheArrayContext =
  createContext<
    (url: string, page: number, response: AxiosResponse<unknown>, maxAge?: number) => void
  >(null);
export const RemoveCacheContext = createContext<(url: string) => void>(null);

function CacheContextProvider({ children }: PropsWithChildren) {
  const [cache, setCache] = useState<CacheMap>(new Map());

  const addCache = (
    url: string,
    response: AxiosResponse<unknown>,
    maxAge = CACHE_TIME.FIVE_MINS
  ) => {
    setCache((prev) => prev.set(url, { response, expires: Date.now() + maxAge }));
  };

  const addCacheArray = (
    url: string,
    page: number,
    response: AxiosResponse<unknown>,
    maxAge = CACHE_TIME.FIVE_MINS
  ) => {
    setCache((prev) => {
      const prevCache = prev.get(url) || [];
      if (!(prevCache instanceof Array)) {
        throw new Error('잘못된 캐시 저장입니다.');
      }
      prevCache[page] = { response, expires: Date.now() + maxAge };
      return prev.set(url, prevCache);
    });
  };

  const getCache = (url: string, page?: number) => {
    const cacheBody = cache.get(url);
    const isCacheArray = cacheBody instanceof Array;

    if (!cacheBody || (isCacheArray && !cacheBody.at(page))) {
      return;
    }
    if (isCacheArray && page === undefined) {
      throw new Error('페이지를 포함해서 캐시를 조회해야 합니다.');
    }

    const expireDate = isCacheArray ? cacheBody.at(page).expires : cacheBody.expires;

    if (expireDate < Date.now()) {
      removeCache(url);

      return;
    }

    return isCacheArray ? cacheBody[page].response : cacheBody.response;
  };

  const removeCache = (url: string) => {
    setCache((prev) => {
      const current = new Map(prev);
      current.delete(url);
      return current;
    });
  };

  return (
    <GetCacheContext.Provider value={getCache}>
      <AddCacheContext.Provider value={addCache}>
        <AddCacheArrayContext.Provider value={addCacheArray}>
          <RemoveCacheContext.Provider value={removeCache}>
            {children}
          </RemoveCacheContext.Provider>
        </AddCacheArrayContext.Provider>
      </AddCacheContext.Provider>
    </GetCacheContext.Provider>
  );
}

export default CacheContextProvider;
