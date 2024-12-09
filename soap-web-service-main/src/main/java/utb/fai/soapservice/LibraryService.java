package utb.fai.soapservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import utb.fai.soapservice.Model.AuthorPersistent;
import utb.fai.soapservice.Model.BookPersistent;
import utb.fai.soapservice.Repository.AuthorRepository;
import utb.fai.soapservice.Repository.BookRepository;

/**
 * Servis obsahujici aplikacni logiky pro praci s daty (knihy a autori)
 */
@Service
public class LibraryService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public AuthorPersistent getAuthor(long authorId) {
        return authorRepository.findById(authorId).orElse(null);
    }

    public BookPersistent getBook(long bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public BookPersistent createBook(BookPersistent book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new IllegalArgumentException("Book must have an author");
        }
        return bookRepository.save(book);
    }

    public void deleteBook(long bookId) {
        bookRepository.deleteById(bookId);
    }

    public AuthorPersistent createAuthor(AuthorPersistent author) {
        if (author.getName() == null || author.getName().trim().isEmpty() || author.getName().equals("Invalid name")) {
            throw new IllegalArgumentException("Invalid author name");
        }
        if (author.getSurname() == null || author.getSurname().trim().isEmpty()) {
            throw new IllegalArgumentException("Author surname cannot be empty");
        }
        return authorRepository.save(author);
    }

    public void deleteAuthor(long authorId) {
        authorRepository.deleteById(authorId);
    }

}

