import * as S from '@/components/common/HeaderLogo/HeaderLogo.style';

import ROUTES from '@/constants/routes';

import HeaderLogoImage from '@/assets/HeaderLogo.svg';
import HeaderLogoImageHorizontal from '@/assets/HeaderLogoHorizontal.svg';

type Props = {
  device: Device;
};
function HeaderLogo({ device }: Props) {
  return (
    <S.Container>
      <S.LinkWrapper to={ROUTES.HOME}>
        {device === 'desktop' ? <HeaderLogoImage /> : <HeaderLogoImageHorizontal />}
      </S.LinkWrapper>
    </S.Container>
  );
}

export default HeaderLogo;
