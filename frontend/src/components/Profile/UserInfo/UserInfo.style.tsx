import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  align-items: center;
  width: max-content;
  gap: 0.5rem;
`;

export const ImageWrapper = styled.div`
  width: 3rem;
  aspect-ratio: 1 / 1;
  border-radius: 50%;
  border: 2px solid ${({ theme }) => theme.colors.gray};
  overflow: hidden;
`;

export const InfoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
`;

export const UserNameWrapper = styled.div`
  display: flex;
  gap: 0.7rem;
`;

export const ProfileImage = styled.img`
  width: 100%;
  object-fit: cover;
`;

export const Username = styled.p`
  font-size: 1.2rem;
  font-weight: 600;
`;

export const ChipWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`;

export const GithubLink = styled.a``;
