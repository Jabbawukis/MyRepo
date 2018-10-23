CREATE DATABASE [Blogging];
GO

USE [Blogging];
GO

CREATE TABLE [Blog] (
    [IMEI] nvarchar NOT NULL IDENTITY,
    [Url] nvarchar(max) NOT NULL,
    CONSTRAINT [PK_Blog] PRIMARY KEY ([IMEI])
);
GO

CREATE TABLE [Post] (
    [IMEI] nvarchar(max) IDENTITY,
    [Longetude] nvarchar(max),
    [Latetude] nvarchar(max),
    CONSTRAINT [PK_Post] PRIMARY KEY ([IMEI]),
    CONSTRAINT [FK_Post_Blog_IMEI] FOREIGN KEY ([IMEI]) REFERENCES [Blog] ([IMEI]) ON DELETE CASCADE
);
GO

INSERT INTO [Blog] (Url) VALUES
('http://blogs.msdn.com/dotnet'),
('http://blogs.msdn.com/webdev'),
('http://blogs.msdn.com/visualstudio')
GO