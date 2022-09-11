import { AxiosResponse } from 'axios';
import { createContext, PropsWithChildren, useState } from 'react';

type CacheBody = { response: AxiosResponse<unknown>; expires: number };
type CacheMap = Map<string, CacheBody>;

export const GetCacheContext =
  createContext<(url: string) => AxiosResponse<unknown, unknown>>(null);
export const AddCacheContext =
  createContext<(url: string, response: AxiosResponse<unknown>) => void>(null);
export const RemoveCacheContext = createContext<(url: string) => void>(null);

function CacheContextProvider({ children }: PropsWithChildren) {
  const [cache, setCache] = useState<CacheMap>(new Map());

  const addCache = (url: string, response: AxiosResponse<unknown>) => {
    setCache((prev) => prev.set(url, { response, expires: Date.now() + 100 * 60 * 5 }));
  };

  const getCache = (url: string) => {
    const cacheBody = cache.get(url);

    if (!cacheBody) {
      return;
    }

    if (cacheBody.expires < Date.now()) {
      removeCache(url);

      return;
    }

    return cacheBody.response;
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
