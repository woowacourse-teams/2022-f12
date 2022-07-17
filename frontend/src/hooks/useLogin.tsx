import { SetUserDataContext } from '@/contexts/LoginContextProvider';
import useGetOne from '@/hooks/api/useGetOne';
import { useContext, useEffect } from 'react';

type UserData = {
  jobType: string;
  career: string;
  accessToken: string;
};

function useLogin(url: string, code: string): UserData {
  const setUserData = useContext(SetUserDataContext);
  const userData = useGetOne<UserData>({
    url: `${url}?code=${code}`,
  });

  useEffect(() => {
    if (!userData) return;

    setUserData(userData);
  }, [userData]);

  return userData;
}

export default useLogin;
