import { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { useContext } from 'react';

import {
  AddCacheContext,
  GetCacheContext,
  RemoveCacheContext,
} from '@/contexts/CacheContextProvider';

type getWithCacheParams = {
  axiosInstance: AxiosInstance;
  url: string;
  config: AxiosRequestConfig;
  maxAge: number;
};

function useCache() {
  const getCache = useContext(GetCacheContext);
  const addCache = useContext(AddCacheContext);
  const removeCache = useContext(RemoveCacheContext);

  const getCachedResponse = (cacheKey: string) => {
    const cachedResponse = getCache(cacheKey);
    if (cachedResponse) {
      return cachedResponse;
    }
  };

  const saveCacheResponse = (
    cacheKey: string,
    response: AxiosResponse,
    maxAge: number
  ) => {
    addCache(cacheKey, response, maxAge);
  };

  const getWithCache = async ({
    axiosInstance,
    url,
    config,
    maxAge,
  }: getWithCacheParams) => {
    const searchParams = new URLSearchParams(config.params as Record<string, string>);
    const hasNoSearchParams = Array.from(searchParams).length === 0;
    const cacheKey = `${url}${hasNoSearchParams ? '' : `?${searchParams.toString()}`}`;
    const cachedResponse = getCachedResponse(cacheKey);
    if (cachedResponse) {
      return cachedResponse;
    }

    const response = await axiosInstance.get(url, config);

    saveCacheResponse(cacheKey, response, maxAge);
    return response;
  };

  return { getWithCache, removeCache };
}

export default useCache;
