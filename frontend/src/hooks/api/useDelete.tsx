import { AxiosRequestHeaders } from 'axios';
import axiosInstance from '@/hooks/api/axiosInstance';

type Props = {
  url: string;
  headers: AxiosRequestHeaders;
};

function useDelete({ url, headers }: Props): (id: number) => Promise<void> {
  const deleteData = async (id: number) => {
    await axiosInstance.delete(url + `/${id}`, {
      headers,
    });
  };

  return deleteData;
}
export default useDelete;
