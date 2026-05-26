export interface Post {
    id: number;
    title: string;
    content: string;
    authorUsername: string;
    topicName: string;
    createdAt: Date;
}

export interface PostComment {
    id: number;
    authorUsername: string;
    content: string;
    createdAt: Date;
}

export interface PostDetail extends Post {
    comments: PostComment[];
}