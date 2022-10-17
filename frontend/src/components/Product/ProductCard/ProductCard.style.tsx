import styled, { css } from 'styled-components';

export const Container = styled.div<{ index: number; size: 's' | 'm' | 'l' }>`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 1rem;

  border-radius: 0.375rem;
  padding: 1rem;
  min-width: 13rem;
  width: ${({ size }) => (size === 'l' ? '19rem' : '15rem')};

  &:hover {
    img {
      transform: scale(1.03);
      transition: 0.2s;
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
  aspect-ratio: 1 / 1;
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

const nameFontSize = {
  s: '0.75rem',
  m: '0.85rem',
  l: '1rem',
};

export const Name = styled.p<{ size: 's' | 'm' | 'l' }>`
  line-height: 1.3;
  font-weight: 500;
  font-size: ${({ size }) => nameFontSize[size]};

  ${Container}:hover & {
    text-decoration: underline;
  }
`;
