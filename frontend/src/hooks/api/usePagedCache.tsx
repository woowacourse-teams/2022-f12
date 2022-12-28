import { AxiosInstance, AxiosRequestConfig } from 'axios';
import { useContext } from 'react';

import { PagedCacheControlContext } from '@/contexts/PagedCacheContextProvider';

type GetWithCacheProps = {
  key: string;
  page: number;
  axiosInstance: AxiosInstance;
  url: string;
  config: AxiosRequestConfig;
};

function usePagedCache() {
  const { getCache, addCache, removeCache } = useContext(PagedCacheControlContext);

  const getWithCache = async ({
    key,
    page,
    axiosInstance,
    url,
    config,
  }: GetWithCacheProps) => {
    const cache = getCache(key) ? [...getCache(key)] : [];

    if (cache[page] !== undefined) {
      return cache;
    }

    const response = await axiosInstance.get(url, config);
    if (response.status >= 400) {
      return cache;
    }

    addCache(key, response);

    cache.push(response);

    return cache;
  };

  return { getWithCache, removeCache };
}

export default usePagedCache;
