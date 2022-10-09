import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;

export const SearchWrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 0.8rem;
`;

export const SearchFilterWrapper = styled.div`
  display: flex;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      flex-direction: column;
      gap: 0.4rem;
      margin-bottom: 1rem;
    }
    @media screen and ${device.tablet} {
      flex-direction: column;
      gap: 0.4rem;
      margin-bottom: 1rem;
    }
    @media screen and ${device.desktop} {
      flex-direction: row;
      gap: 1rem;
      margin-bottom: 0.4rem;
    }
  `}
`;
