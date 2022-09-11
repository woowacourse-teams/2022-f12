import { AxiosInstance, AxiosRequestConfig } from 'axios';
import { useContext } from 'react';

import {
  AddCacheContext,
  GetCacheContext,
  RemoveCacheContext,
} from '@/contexts/CacheContextProvider';

type getWithCacheParams = {
  axiosInstance: AxiosInstance;
  url: string;
  config?: AxiosRequestConfig;
};

function useCache() {
  const getCache = useContext(GetCacheContext);
  const addCache = useContext(AddCacheContext);
  const removeCache = useContext(RemoveCacheContext);

  const getWithCache = async ({ axiosInstance, url, config }: getWithCacheParams) => {
    const searchParams = new URLSearchParams(config.params as Record<string, string>);
    const completeUrl = `${url}${searchParams.toString()}`;

    const cachedResponse = getCache(completeUrl);

    if (cachedResponse) {
      return cachedResponse;
    }

    const response = await axiosInstance.get(url, config);
    addCache(completeUrl, response);

    return response;
  };

  return { getWithCache, removeCache };
}

export default useCache;
