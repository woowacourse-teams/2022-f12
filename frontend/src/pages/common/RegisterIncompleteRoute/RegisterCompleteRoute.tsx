import { useContext, useLayoutEffect } from 'react';

import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useAuth from '@/hooks/useAuth';
import useModal from '@/hooks/useModal';

import { VALIDATION_ERROR_MESSAGES } from '@/constants/messages';
import ROUTES from '@/constants/routes';

function RegisterCompleteRoute() {
  const { isLoggedIn } = useAuth();
  const userData = useContext(UserDataContext);
  const { showAlert } = useModal();

  const isRegistered = !isLoggedIn || (isLoggedIn && userData?.registerCompleted);

  useLayoutEffect(() => {
    (async () => {
      if (!isRegistered) {
        await showAlert(VALIDATION_ERROR_MESSAGES.ADDITIONAL_INFO_REQUIRED);
      }
    })().catch(() => {
      window.alert(VALIDATION_ERROR_MESSAGES.ADDITIONAL_INFO_REQUIRED);
    });
  });

  return <ProtectedRoute when={isRegistered} redirectPath={ROUTES.REGISTER} />;
}

export default RegisterCompleteRoute;
