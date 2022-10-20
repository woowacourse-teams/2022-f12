import { Link } from 'react-router-dom';
import styled from 'styled-components';

export const Container = styled.div`
  height: max-content;

  display: flex;
  align-items: center;
  justify-content: center;

  position: relative;

  background-color: ${({ theme }) => theme.colors.white};
`;

export const LogoLink = styled(Link)`
  padding: 1rem;
  width: 11rem;
`;

export const CustomLink = styled(Link)`
  padding: 1rem;
  width: 5rem;

  text-align: center;

  svg {
    width: 2em;
  }
`;
