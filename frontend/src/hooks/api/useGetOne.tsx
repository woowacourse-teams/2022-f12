import { AxiosRequestHeaders, AxiosResponse } from 'axios';
import { useState, useEffect } from 'react';
import axiosInstance from '@/hooks/api/axiosInstance';

type Props = {
  url: string;
  headers?: null | AxiosRequestHeaders;
};

function useGetOne<T>({ url, headers }: Props): [T, () => void] {
  const [data, setData] = useState<null | T>(null);
  const [refetchTrigger, setRefetchTrigger] = useState(0);

  const fetchData = async () => {
    const { data }: AxiosResponse<T> = await axiosInstance.get(url, {
      headers,
    });

    return data;
  };
  const refetch = () => {
    setRefetchTrigger((prevValue) => prevValue + 1);
  };

  useEffect(() => {
    fetchData()
      .then((data) => {
        !!data && setData(data);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [refetchTrigger]);

  return [data, refetch];
}
export default useGetOne;
