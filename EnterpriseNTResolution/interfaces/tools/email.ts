interface Email {
    recipients: string[];
    subject: string;
    body: string;
    attachment?: Image[];
    order?: string
    tests?: string,
    user: number
}