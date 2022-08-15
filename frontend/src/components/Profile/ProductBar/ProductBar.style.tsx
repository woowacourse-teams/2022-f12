import styled, { DefaultTheme } from 'styled-components';

const borderByType = (theme: DefaultTheme, type: 'default' | 'selected' | 'add') => {
  if (type === 'default') return `2px solid ${theme.colors.secondary}`;
  if (type === 'selected') return `3px solid ${theme.colors.primary}`;
  if (type === 'add') return `3px dashed ${theme.colors.primary}`;
};

export const Container = styled.div<{
  barType: 'default' | 'selected' | 'add';
}>`
  width: 100%;
  height: 2.5rem;

  display: flex;
  justify-content: center;
  align-items: center;

  border-radius: 0.5rem;
  border: ${({ theme, barType }) => borderByType(theme, barType)};
  background-color: ${({ theme }) => theme.colors.white};
`;

export const Name = styled.p`
  width: 34rem;

  font-size: 1rem;
  font-weight: 600;
  text-align: center;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

export const AddContainer = styled(Container)`
  width: 34rem;
  cursor: pointer;
`;
