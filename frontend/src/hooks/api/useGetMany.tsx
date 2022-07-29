import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';
import useAxios from '@/hooks/api/useAxios';

type SearchParams = Record<string, string>;

type GetManyParams = SearchParams & { size: string };

type Props = {
  url: string;
  params?: GetManyParams;
  body?: null | object;
  headers?: null | AxiosRequestHeaders;
};

type Data<T> = {
  hasNext: boolean;
  items: T[];
};

type Return<T> = {
  data: T[];
  isLoading: boolean;
  isReady: boolean;
  getNextPage: () => void;
  refetch: () => void;
};

function useGetMany<T>({ url, params, body, headers }: Props): Return<T> {
  const [data, setData] = useState<T[]>([]);

  const [page, setPage] = useState(0);
  const [hasNextPage, setHasNextPage] = useState(true);

  const [nextPageTrigger, setNextPageTrigger] = useState(0);
  const [refetchTrigger, setRefetchTrigger] = useState(0);

  const [axiosInstance, isLoading] = useAxios();

  const fetchData = async () => {
    const {
      data: { hasNext, items },
    }: AxiosResponse<Data<T>> = await axiosInstance.get(url, {
      data: body,
      headers,
      params: {
        page,
        ...params,
      },
    });

    return { hasNext, items };
  };

  const getNextPage = () => {
    console.log('getting next page');
    setNextPageTrigger((prevTrigger) => prevTrigger + 1);
  };

  const refetch = () => {
    console.log('refetching');
    setRefetchTrigger((prevTrigger) => prevTrigger + 1);
    setPage(0);
    setHasNextPage(true);
    setData([]);
  };

  useEffect(() => {
    if (!hasNextPage || isLoading) return;

    fetchData()
      .then(({ hasNext, items }) => {
        !!items && setData((prevData) => [...prevData, ...items]);
        return hasNext;
      })
      .then((hasNext) => {
        if (hasNext) {
          setPage((prevPage) => prevPage + 1);
        } else {
          setHasNextPage(false);
        }
      })
      .catch((error) => {
        console.log(error);
      });
  }, [nextPageTrigger, refetchTrigger]);

  const searchParams = new URLSearchParams(params);

  useEffect(() => {
    if (refetchTrigger === 0) return; // 최초 렌더링 시 refetch 방지 임시 조치
    refetch();
  }, [searchParams.toString()]);

  const isReady = data.length !== 0;

  return { data, getNextPage, refetch, isLoading, isReady };
}
export default useGetMany;
