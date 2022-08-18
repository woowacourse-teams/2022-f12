import useDelete from '@/hooks/api/useDelete';
import usePost from '@/hooks/api/usePost';
import useModal from '@/hooks/useModal';

import { ENDPOINTS } from '@/constants/api';

type Return = {
  followUser: () => Promise<void>;
  unfollowUser: () => Promise<void>;
};

function useFollowing(memberId: number): Return {
  const { showAlert } = useModal();

  const postFollow = usePost({
    url: `${ENDPOINTS.FOLLOWING(memberId)}`,
  });

  const deleteFollow = useDelete({ url: ENDPOINTS.FOLLOWING });

  const followUser = async () => {
    try {
      await postFollow(memberId);
      await showAlert('팔로우 완료!');
    } catch {
      throw new Error('팔로우 실패');
    }
  };

  const unfollowUser = async () => {
    try {
      await deleteFollow(memberId);
      await showAlert('팔로우를 취소합니다.');
    } catch (e) {
      throw new Error('팔로우 취소 실패');
      return;
    }
  };

  return { followUser, unfollowUser };
}
export default useFollowing;
