import { useContext } from 'react';

import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAuth from '@/hooks/useAuth';
import useModal from '@/hooks/useModal';

import ROUTES from '@/constants/routes';

function RegisterCompleteRoute() {
  const { isLoggedIn } = useAuth();
  const userData = useContext(UserDataContext);
  const { showAlert } = useModal();

  const isRegistered = !isLoggedIn || (isLoggedIn && userData?.registerCompleted);

  if (!isRegistered) showAlert('추가 정보 입력 후 이용해주세요.');

  return <ProtectedRoute when={isRegistered} redirectPath={ROUTES.REGISTER} />;
}

export default RegisterCompleteRoute;
