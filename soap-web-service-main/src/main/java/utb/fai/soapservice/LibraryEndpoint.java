package utb.fai.soapservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.book_web_service.Author;
import com.example.book_web_service.Book;
import com.example.book_web_service.GetBookRequest;
import com.example.book_web_service.GetBookResponse;

// Nutné nejprve nadefinovat CreateBookRequest a CreateBookResponse v souboru books.xsd, aby se třídy vygenerovaly a import byl funkční.
import com.example.book_web_service.CreateBookRequest;
import com.example.book_web_service.CreateBookResponse;
import com.example.book_web_service.UpdateBookRequest;
import com.example.book_web_service.UpdateBookResponse;

import utb.fai.soapservice.Model.AuthorPersistent;
import utb.fai.soapservice.Model.BookPersistent;

import com.example.book_web_service.DeleteBookRequest;
import com.example.book_web_service.DeleteBookResponse;
import com.example.book_web_service.GetAuthorRequest;
import com.example.book_web_service.GetAuthorResponse;
import com.example.book_web_service.CreateAuthorRequest;
import com.example.book_web_service.CreateAuthorResponse;
import com.example.book_web_service.DeleteAuthorRequest;
import com.example.book_web_service.DeleteAuthorResponse;

@Endpoint
public class LibraryEndpoint {
    private static final String NAMESPACE_URI = "http://example.com/book-web-service";

    @Autowired
    private LibraryService libraryService;

    // Metoda pro získání knihy podle ID
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBookRequest")
    @ResponsePayload
    public GetBookResponse getBook(@RequestPayload GetBookRequest request) {
        long bookId = request.getBookId();
        BookPersistent book = libraryService.getBook(bookId);

        GetBookResponse response = new GetBookResponse();
        if (book != null) {
            Book bookResponse = new Book();
            bookResponse.setId(book.getId());
            bookResponse.setTitle(book.getTitle());
            bookResponse.setAuthorID(book.getAuthor().getId());
            response.setBook(bookResponse);
        } else {
            throw new IllegalArgumentException("Neplatné ID autora");
        }
        return response;
    }

    // Metoda pro vytvoření nové knihy
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createBookRequest")
    @ResponsePayload
    public CreateBookResponse createBook(@RequestPayload CreateBookRequest request) {
        BookPersistent book = new BookPersistent();
        book.setTitle(request.getBook().getTitle());

        // Získání autora podle ID
        AuthorPersistent author = libraryService.getAuthor(request.getBook().getAuthorID());
        if (author == null) {
            throw new IllegalArgumentException("Neplatné ID autora");
        }
        book.setAuthor(author);

        try {
            book = libraryService.createBook(book);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Neplatné ID autora");
        }

        CreateBookResponse response = new CreateBookResponse();
        Book bookResponse = new Book();
        bookResponse.setId(book.getId());
        bookResponse.setTitle(book.getTitle());
        bookResponse.setAuthorID(book.getAuthor().getId());
        response.setBook(bookResponse);

        return response;
    }

    // Metoda pro aktualizaci knihy
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateBookRequest")
    @ResponsePayload
    public UpdateBookResponse updateBook(@RequestPayload UpdateBookRequest request) {
        BookPersistent book = libraryService.getBook(request.getBookId());
        if (book != null) {
            book.setTitle(request.getBook().getTitle());
            AuthorPersistent author = libraryService.getAuthor(request.getBook().getAuthorID());
            if (author != null) {
                book.setAuthor(author);
            }
            try {
                BookPersistent updatedBook = libraryService.createBook(book);

                UpdateBookResponse response = new UpdateBookResponse();
                Book bookResponse = new Book();
                bookResponse.setId(updatedBook.getId());
                bookResponse.setTitle(updatedBook.getTitle());
                bookResponse.setAuthorID(updatedBook.getAuthor().getId());
                response.setBook(bookResponse);
                return response;
            } catch (IllegalArgumentException e) {
                UpdateBookResponse response = new UpdateBookResponse();
                return response;
            }
        }
        return null;
    }

    // Metoda pro smazání knihy
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteBookRequest")
    @ResponsePayload
    public DeleteBookResponse deleteBook(@RequestPayload DeleteBookRequest request) {
        long bookId = request.getBookId();
        libraryService.deleteBook(bookId);

        DeleteBookResponse response = new DeleteBookResponse();
        response.setMessage("Kniha byla úspěšně smazána");
        return response;
    }

    // Metoda pro získání autora podle ID
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAuthorRequest")
    @ResponsePayload
    public GetAuthorResponse getAuthor(@RequestPayload GetAuthorRequest request) {
        long authorId = request.getAuthorId();
        AuthorPersistent author = libraryService.getAuthor(authorId);

        GetAuthorResponse response = new GetAuthorResponse();
        if (author != null) {
            Author authorResponse = new Author();
            authorResponse.setId(author.getId());
            authorResponse.setName(author.getName());
            authorResponse.setSurname(author.getSurname());
            response.setAuthor(authorResponse);
        } else {
            throw new IllegalArgumentException("Neplatné ID autora");
        }
        return response;
    }

    // Metoda pro vytvoření nového autora
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createAuthorRequest")
    @ResponsePayload
    public CreateAuthorResponse createAuthor(@RequestPayload CreateAuthorRequest request) {
        AuthorPersistent author = new AuthorPersistent();
        author.setName(request.getAuthor().getName());
        author.setSurname(request.getAuthor().getSurname());

        try {
            author = libraryService.createAuthor(author);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Neplatné ID autora");
        }

        CreateAuthorResponse response = new CreateAuthorResponse();
        Author authorResponse = new Author();
        authorResponse.setId(author.getId());
        authorResponse.setName(author.getName());
        authorResponse.setSurname(author.getSurname());
        response.setAuthor(authorResponse);

        return response;
    }

    // Metoda pro smazání autora
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAuthorRequest")
    @ResponsePayload
    public DeleteAuthorResponse deleteAuthor(@RequestPayload DeleteAuthorRequest request) {
        long authorId = request.getAuthorId();
        libraryService.deleteAuthor(authorId);

        DeleteAuthorResponse response = new DeleteAuthorResponse();
        response.setMessage("Autor byl úspěšně smazán");
        return response;
    }
}
