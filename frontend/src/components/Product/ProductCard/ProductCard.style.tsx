import styled, { css } from 'styled-components';

export const Container = styled.article<{ index: number }>`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 1rem;

  border-radius: 0.375rem;
  padding: 1rem;
  min-width: 13rem;

  ${({ theme: { device } }) => css`
    @media screen and ${device.tablet} {
      width: 15rem;
    }
    @media screen and ${device.desktop} {
      width: 19rem;
    }
  `}

  &:hover {
    img {
      transform: scale(1.03);
      transition: 0.2s;
    }
    h2 {
      text-decoration: underline;
    }
  }

  ${({ index }) => css`
    animation: fade-in-${index} ${500 + index * 50}ms;

    @keyframes fade-in-${index} {
      0% {
        transform: translateY(-10px);
        scale: 1.1;
        opacity: 0;
      }

      ${index * 5}% {
        transform: translateY(-10px);
        scale: 1.1;
        opacity: 0;
      }
    }
  `}
`;
export const ImageWrapper = styled.div`
  width: 100%;
  overflow: hidden;
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  background-color: #fff;
  display: flex;
  align-items: center;
`;

export const Image = styled.img`
  width: 100%;
  object-fit: cover;
`;

export const BottomWrapper = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Name = styled.h2`
  line-height: 1.3;
  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      font-size: 0.75rem;
    }
    @media screen and ${device.tablet} {
      font-size: 0.85rem;
    }
    @media screen and ${device.desktop} {
      font-size: 1rem;
    }
  `}
`;
