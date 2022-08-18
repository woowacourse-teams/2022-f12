import useDelete from './api/useDelete';
import usePost from './api/usePost';
import useSessionStorage from './useSessionStorage';

import { ENDPOINTS } from '@/constants/api';

type Props = {
  memberId: string;
};

function useFollowing({ memberId }: Props) {
  const [data] = useSessionStorage<UserData>('userData');

  const hasToken = data && data.token !== undefined;

  const followUser = usePost({
    url: `${ENDPOINTS.FOLLOWING(memberId)}`,
    headers: hasToken ? { Authorization: `Bearer ${data.token}` } : null,
  });

  const unFollowUser = useDelete({
    url: `${ENDPOINTS.FOLLOWING(memberId)}`,
    headers: hasToken ? { Authorization: `Bearer ${data.token}` } : null,
  });

  return { followUser, unFollowUser };
}
export default useFollowing;
