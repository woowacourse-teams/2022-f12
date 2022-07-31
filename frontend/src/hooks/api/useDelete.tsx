import { AxiosRequestHeaders } from 'axios';
import useAxios from '@/hooks/api/useAxios';
import useError from '@/hooks/useError';

type Props = {
  url: string;
  headers: AxiosRequestHeaders;
};

function useDelete({ url, headers }: Props): (id: number) => Promise<void> {
  const { axiosInstance } = useAxios();
  const handleError = useError();

  const deleteData = async (id: number) => {
    try {
      await axiosInstance.delete(`${url}/${id}`, {
        headers,
      });
    } catch (error) {
      handleError(error as Error, `token: ${headers.Authorization.toString()}`);
    }
  };

  return deleteData;
}
export default useDelete;
