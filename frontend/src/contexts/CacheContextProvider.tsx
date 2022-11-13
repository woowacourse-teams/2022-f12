import { AxiosResponse } from 'axios';
import { createContext, PropsWithChildren, useState } from 'react';

import { CACHE_TIME } from '@/constants/cache';

type CacheBody = {
  response: AxiosResponse<unknown> | AxiosResponse<unknown>[];
  expires: number;
};
type CacheMap = Map<string, CacheBody | CacheBody[]>;

export const GetCacheContext = createContext<
  | null
  | ((
      url: string,
      page?: number
    ) => AxiosResponse<unknown, unknown> | AxiosResponse<unknown, unknown>[] | undefined)
>(null);
export const AddCacheContext = createContext<
  null | ((url: string, response: AxiosResponse<unknown>, maxAge?: number) => void)
>(null);
export const AddCacheArrayContext = createContext<
  | null
  | ((
      url: string,
      page: number,
      response: AxiosResponse<unknown>,
      maxAge?: number
    ) => void)
>(null);
export const RemoveCacheContext = createContext<null | ((url: string) => void)>(null);

function CacheContextProvider({ children }: PropsWithChildren) {
  const [cache, setCache] = useState<CacheMap>(new Map());

  const addCache = (
    url: string,
    response: AxiosResponse<unknown>,
    maxAge = CACHE_TIME.ONE_MINS
  ) => {
    setCache((prev) => prev.set(url, { response, expires: Date.now() + maxAge }));
  };

  const addCacheArray = (
    url: string,
    page: number,
    response: AxiosResponse<unknown>,
    maxAge = CACHE_TIME.ONE_MINS
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

  const isCacheBodyArray = (
    input: CacheBody | CacheBody[] | undefined
  ): input is CacheBody[] => Array.isArray(input);

  const handleCacheExpiry = (cache: CacheBody, url: string) => {
    if (cache.expires < Date.now()) {
      removeCache(url);

      return;
    }
    return cache.response;
  };

  const getCache = (url: string, page?: number) => {
    const cacheBody = cache.get(url);
    const isCacheArray = isCacheBodyArray(cacheBody);
    if (cacheBody === undefined) {
      return;
    }
    if (!isCacheArray) {
      return handleCacheExpiry(cacheBody, url);
    }
    if (page === undefined) {
      throw new Error('페이지를 포함해서 캐시를 조회해야 합니다.');
    }
    if (cacheBody[page] === undefined) {
      return;
    }

    const pageCacheBody = cacheBody[page];

    return handleCacheExpiry(pageCacheBody, url);
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
