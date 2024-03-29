import { Link } from 'react-router-dom';
import styled, { css } from 'styled-components';

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  gap: 1rem;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      min-height: 20rem;
    }
    @media screen and ${device.desktop} {
      min-height: 28rem;
    }
  `}
`;

export const FlexWrapper = styled.div`
  display: flex;
  overflow-y: hidden;
  overflow-x: scroll;

  ${({ theme: { device } }) => css`
    @media screen and ${device.mobile} {
      min-height: 20rem;
    }
    @media screen and ${device.desktop} {
      min-height: 28rem;
      justify-content: space-between;
      -ms-overflow-style: none;
      scrollbar-width: none;
      &::-webkit-scrollbar {
        display: none;
      }
    }
  `}
`;

export const Grid = styled.ul<{ columnCount: number }>`
  display: grid;
  grid-template-columns: repeat(${({ columnCount }) => columnCount}, 1fr);
`;

export const Title = styled.h1`
  font-size: 1.5rem;
`;

export const CustomLink = styled(Link)``;

export const ProductLink = styled(Link)`
  display: flex;
  justify-content: center;
  width: max-content;
  margin: 0 auto;
`;

export const Wrapper = styled.div`
  margin: 0 auto;
  width: 100%;
`;

export const NoDataContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

export const ProductCardLi = styled.li`
  width: max-content;
  margin: 0 auto;
`;
