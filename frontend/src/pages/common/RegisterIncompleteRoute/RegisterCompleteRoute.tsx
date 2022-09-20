import { useContext, useLayoutEffect } from 'react';

import ProtectedRoute from '@/pages/common/ProtectedRoute/ProtectedRoutes';

import { UserDataContext } from '@/contexts/LoginContextProvider';

import useModal from '@/hooks/useModal';

import { VALIDATION_ERROR_MESSAGES } from '@/constants/messages';
import ROUTES from '@/constants/routes';

function RegisterCompleteRoute() {
  const userData = useContext(UserDataContext);
  const { showAlert } = useModal();

  const isRegistered =
    !userData || userData.registerCompleted === undefined || userData.registerCompleted;

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
