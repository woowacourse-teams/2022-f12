import styled, { css } from 'styled-components';

export const Container = styled.div`
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-direction: column;
  max-width: 1320px;
  width: 100%;
  margin: auto;
  padding: 50px 0;
  gap: 3rem;

  ${({ theme: { device } }) => css`
    @media screen and ${device.desktop} {
      flex-direction: row;
      justify-content: space-around;
      align-items: flex-start;
    }
  `}
`;

export const ProductDetailWrapper = styled.div`
  width: 30rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      width: 100%;
    }
  `}
`;

export const ReviewListWrapper = styled.div`
  position: relative;

  display: flex;
  flex-direction: column;
  width: 600px;
  height: 100%;
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      width: 100%;
    }
  `}
`;

export const ReviewListContainer = styled.section`
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;

  min-height: 10rem;
`;
