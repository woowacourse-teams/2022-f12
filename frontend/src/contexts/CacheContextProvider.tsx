import { AxiosResponse } from 'axios';
import { createContext, PropsWithChildren, useState } from 'react';

import { CACHE_TIME } from '@/constants/cache';

type CacheBody = {
  response: AxiosResponse<unknown>;
  expires: number;
};
type CacheMap = Map<string, CacheBody>;

export const GetCacheContext = createContext<
  null | ((url: string) => AxiosResponse<unknown, unknown> | undefined)
>(null);
export const AddCacheContext = createContext<
  null | ((url: string, response: AxiosResponse<unknown>, maxAge?: number) => void)
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

  const handleCacheExpiry = (cache: CacheBody, url: string) => {
    if (cache.expires < Date.now()) {
      removeCache(url);

      return;
    }
    return cache.response;
  };

  const getCache = (url: string) => {
    const cacheBody = cache.get(url);
    if (cacheBody === undefined) {
      return;
    }

    return handleCacheExpiry(cacheBody, url);
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
        <RemoveCacheContext.Provider value={removeCache}>
          {children}
        </RemoveCacheContext.Provider>
      </AddCacheContext.Provider>
    </GetCacheContext.Provider>
  );
}

export default CacheContextProvider;
