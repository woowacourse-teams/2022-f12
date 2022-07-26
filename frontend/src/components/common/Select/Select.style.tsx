import styled from 'styled-components';

export const Container = styled.select`
  width: 150px;
  border: none;
  border-bottom: 1px solid black;
  padding: 10px;
  background-color: ${({ theme }) => theme.colors.white};
`;

export const Option = styled.option``;
