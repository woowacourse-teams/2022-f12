import axios, { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';

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

  const [page, setPage] = useState<number>(0);
  const [hasNextPage, setHasNextPage] = useState<boolean>(true);
  const [nextPageTrigger, setNextPageTrigger] = useState(0);

  const fetchData = async () => {
    if (!hasNextPage) return;
    const {
      data: { hasNext, items },
    }: AxiosResponse<Data<T>> = await axios.get(url, {
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
      });
  }, [nextPageTrigger, sort]);

  return [data, getNextPage];
}
export default useGetMany;
