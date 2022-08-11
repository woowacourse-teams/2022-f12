import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
`;

export const ImageWrapper = styled.div`
  width: 30%;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
  border: 2px solid ${({ theme }) => theme.colors.gray};
  overflow: hidden;
`;
export const ProfileImage = styled.img`
  width: 100%;
  object-fit: cover;
`;

export const Username = styled.p`
  font-size: 1.5rem;
`;

export const ChipWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`;

export const GithubLink = styled.a``;
