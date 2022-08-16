import styled from 'styled-components';

const cardSize = {
  s: {
    width: '170px',
    height: '210px',
    titleFontSize: '0.7rem',
    titleHeight: '20px',
    buttonFontSize: '0.5rem',
    borderColor: '${({ theme }) => theme.colors.white}',
  },
  l: {
    width: '230px',
    height: '290px',
    titleFontSize: '1rem',
    titleHeight: '2rem',
    buttonFontSize: '0.8rem',
    borderColor: '${({ theme }) => theme.colors.white}',
  },
};

export const Container = styled.div<{ size: 's' | 'l' }>`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;

  ${({ size }) => `
    width: ${cardSize[size].width};
    height: ${cardSize[size].height};
  `}

  padding: 0.8rem;
  background-color: #fff;
  border-radius: 0.3rem;
  border: 2px solid ${({ theme }) => theme.colors.secondary};
`;

export const ImageWrapper = styled.div`
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  margin-bottom: 0.45rem;
  width: 100%;
`;

export const ProductImage = styled.img`
  width: 100%;
  object-fit: contain;
`;

export const ProductTitle = styled.p<{ size: 's' | 'l' }>`
  width: 100%;
  ${({ size }) => `
    height: ${cardSize[size].titleHeight};
    font-size: ${cardSize[size].titleFontSize};
    `};
  overflow: hidden;
`;

export const ReviewOpenButton = styled.button<{ size: 's' | 'l' }>`
  width: max-content;
  background: none;
  text-decoration: underline;
  margin-top: 0.2rem;
  ${({ size }) => `
    font-size: ${cardSize[size].buttonFontSize};
  `}
  color: ${({ theme }) => theme.colors.black};
`;
