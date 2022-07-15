import useGetOne from '@/hooks/api/useGetOne';

type UserData = {
  jobType: string;
  career: string;
  accessToken: string;
};

function useLogin(url: string, code: string): UserData {
  const userData = useGetOne<UserData>({
    url: `${url}?code=${code}`,
  });

  return userData;
}

export default useLogin;
