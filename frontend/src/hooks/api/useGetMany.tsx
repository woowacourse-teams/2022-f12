import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';
import axiosInstance from './axiosInstance';

type Props = {
  url: string;
  size: number;
  sort?: string;
  body?: null | object;
  headers?: null | AxiosRequestHeaders;
};

type Data<T> = {
  hasNext: boolean;
  items: T[];
};

function useGetMany<T>({
  url,
  size,
  sort,
  body,
  headers,
}: Props): [T[], () => void] {
  const [data, setData] = useState<T[]>([]);

  const [page, setPage] = useState(0);
  const [hasNextPage, setHasNextPage] = useState(true);
  const [nextPageTrigger, setNextPageTrigger] = useState(0);

  const [isLoading, setLoading] = useState(false);

  const fetchData = async () => {
    if (!hasNextPage || isLoading) return;

    setLoading(true);

    const {
      data: { hasNext, items },
    }: AxiosResponse<Data<T>> = await axiosInstance.get(url, {
      data: body,
      headers,
      params: {
        size,
        page,
        sort,
      },
    });

    if (hasNext) {
      setPage((prevPage) => prevPage + 1);
    } else {
      setHasNextPage(false);
    }

    return items;
  };

  const getNextPage = () => {
    setNextPageTrigger((prevTrigger) => prevTrigger + 1);
  };

  useEffect(() => {
    fetchData()
      .then((data) => {
        !!data && setData((prevData) => [...prevData, ...data]);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [nextPageTrigger, sort]);

  return [data, getNextPage];
}
export default useGetMany;
