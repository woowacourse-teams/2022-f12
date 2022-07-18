import styled, { DefaultTheme } from 'styled-components';

const borderByType = (
  theme: DefaultTheme,
  type: 'default' | 'selected' | 'add'
) => {
  if (type === 'default') return `2px solid ${theme.colors.secondary}`;
  if (type === 'selected') return `3px solid ${theme.colors.primary}`;
  if (type === 'add') return `3px dashed ${theme.colors.primary}`;
};

export const Container = styled.div<{
  barType: 'default' | 'selected' | 'add';
}>`
  width: 30rem;
  min-height: 3rem;
  height: max-content;

  display: flex;
  justify-content: center;
  align-items: center;

  font-size: 1.25rem;
  font-weight: 600;
  text-align: left;

  border-radius: 0.5rem;
  border: ${({ theme, barType }) => borderByType(theme, barType)};
  background-color: ${({ theme }) => theme.colors.white};
`;

export const AddContainer = styled(Container)`
  cursor: pointer;
`;
