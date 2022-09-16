import { Link } from 'react-router-dom';
import styled, { css, DefaultTheme } from 'styled-components';

const borderByType = (
  theme: DefaultTheme,
  type: 'default' | 'selected' | 'selectedAnimation'
) => {
  if (type === 'default') return `2px solid ${theme.colors.secondary}`;
  if (type === 'selected') return `3px solid ${theme.colors.primary}`;
  if (type === 'selectedAnimation') return `3px solid ${theme.colors.primary}`;
};

const animationByBorderType = (type: 'default' | 'selected' | 'selectedAnimation') => {
  if (type === 'selectedAnimation') return 'focus linear 1.5s infinite;';
  return 'none';
};

const cardSize = {
  s: {
    width: '170px',
    height: '235px',
    imageHeight: '140px',
    titleFontSize: '0.9rem',
    titleHeight: '1.8rem',
    buttonFontSize: '0.5rem',
    borderColor: '${({ theme }) => theme.colors.white}',
  },
  l: {
    width: '230px',
    height: '305px',
    imageHeight: '200px',
    titleFontSize: '1rem',
    titleHeight: '2rem',
    buttonFontSize: '0.8rem',
    borderColor: '${({ theme }) => theme.colors.white}',
  },
};

export const Container = styled.div<{
  index: number;
  size: 's' | 'l';
  borderType: 'default' | 'selected' | 'selectedAnimation';
  isEditMode: boolean;
}>`
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

  border: ${({ theme, borderType }) => borderByType(theme, borderType)};
  animation: ${({ borderType }) => animationByBorderType(borderType)};

  @keyframes focus {
    0% {
      box-shadow: 0 0 5px red;
    }
    20% {
      box-shadow: 0 0 5px orange;
    }
    40% {
      box-shadow: 0 0 5px yellow;
    }
    60% {
      box-shadow: 0 0 5px green;
    }
    80% {
      box-shadow: 0 0 5px blue;
    }
    100% {
      box-shadow: 0 0 4px purple;
    }
  }

  position: relative;

  ${({ isEditMode }) =>
    isEditMode &&
    css`
      &::after {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
      }
    `}

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

export const ImageWrapper = styled.div<{ size: 's' | 'l' }>`
  border: 1px solid ${({ theme }) => theme.colors.secondary};
  ${({ size }) => `
    height: ${cardSize[size].imageHeight};
  `}
  margin-bottom: 0.45rem;
  width: 100%;
`;

export const ProductImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: contain;
`;

export const ProductTitleWrapper = styled.div<{ size: 's' | 'l' }>`
  ${({ size }) => `
    height: ${cardSize[size].titleHeight};
    `};
`;

export const ProductTitle = styled.p<{ size: 's' | 'l' }>`
  width: 100%;
  ${({ size }) => `
    font-size: ${cardSize[size].titleFontSize};
    `};
  height: 100%;
  overflow: hidden;
  margin-bottom: 0.3rem;
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

export const CustomLink = styled(Link)``;
