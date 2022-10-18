import { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';
import { useContext } from 'react';

import {
  AddCacheArrayContext,
  AddCacheContext,
  GetCacheContext,
  RemoveCacheContext,
} from '@/contexts/CacheContextProvider';

type getWithCacheParams = {
  axiosInstance: AxiosInstance;
  url: string;
  config?: AxiosRequestConfig;
  maxAge: number;
};

function useCache() {
  const getCache = useContext(GetCacheContext);
  const addCache = useContext(AddCacheContext);
  const addCacheArray = useContext(AddCacheArrayContext);
  const removeCache = useContext(RemoveCacheContext);

  const getCachedResponse = (cacheKey: string, page?: number) => {
    const cachedResponse = getCache(cacheKey, page);
    const isCacheArray = cachedResponse instanceof Array;
    if (isCacheArray && cachedResponse[page] !== undefined) {
      return cachedResponse[page];
    }
    if (!isCacheArray && cachedResponse) {
      return cachedResponse;
    }
  };

  const saveCacheResponse = (
    cacheKey: string,
    response: AxiosResponse,
    maxAge: number,
    page?: number
  ) => {
    if (page !== undefined || page !== null) {
      addCacheArray(cacheKey, page, response, maxAge);
    } else {
      addCache(cacheKey, response, maxAge);
    }
  };

  const getWithCache = async ({
    axiosInstance,
    url,
    config,
    maxAge,
  }: getWithCacheParams) => {
    const searchParams = new URLSearchParams(config.params as Record<string, string>);
    const page = Number(searchParams.get('page'));
    searchParams.delete('page');
    const cacheKey = `${url}?${searchParams.toString()}`;

    const cachedResponse = getCachedResponse(cacheKey, page);
    if (cachedResponse) {
      return cachedResponse;
    }

    const response = await axiosInstance.get(url, config);

    saveCacheResponse(cacheKey, response, maxAge, page);
    return response;
  };

  return { getWithCache, removeCache };
}

export default useCache;
