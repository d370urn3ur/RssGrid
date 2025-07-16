package the.autarch.newsgrid.entry.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import the.autarch.newsgrid.channel.data.ChannelEntity

@Entity(
    tableName = "entry",
    foreignKeys = [
        ForeignKey(
            entity = ChannelEntity::class,
            parentColumns = arrayOf("link"),
            childColumns = arrayOf("channelId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        )
    ],
)
data class EntryEntity(
    @PrimaryKey override val link: String,
    @ColumnInfo(index = true) val channelId: String,
    override val title: String,
    override val description: String?,
    override val content: String?,
    override val pubDate: String?,
    override val timestamp: Long?,
//    val updated: String?,
    override val author: String?,
    val imageUrl: String?,
    val source: String?
): Entry {
    companion object
}

//@Serializable
//data class Entry(
//    val title: String,
//    val author: String?,
//    val link: String,   // unique
//    val pubDate: String?,
//    val description: String?,
//    val content: String?,
//    val image: String?,
////    val audio: String?,
////    val video: String?,
////    val sourceName: String?,
////    val sourceUrl: String?,
////    val categories: List<String>,
////    val rawEnclosure: RawEnclosure,
//) {
//    companion object
//}

//class EntryFlutter {
//
//    static Entry? fromItem(Item item, int channelId) {
//
//        final title = item.title;
//        final link = item.link?.href ?? item.links.firstOrNull?.href;
//        if (title == null || link == null) {
//            return null;
//        }
//
//        final author = item.authors.map((author) => author.name).join(',');
//        final imageUrl = Entry.parseImageUrl(item);
//
//        return Entry(
//            null,
//            channelId,
//            title,
//            item.description,
//            item.content.firstOrNull?.value,
//            link,
//            item.published?.parseValue(),
//            item.updated?.parseValue(),
//            author,
//            imageUrl,
//            item.source?.href,
//        );
//    }
//
//    /// Try to obtain a displayable image from com [Item]
//    static String? parseImageUrl(Item item) {
//
//        // Prefer item image
//        var imageUrl = item.image?.url;
//
//        // Otherwise try to use first thumbnail
//        imageUrl ??= item.media?.thumbnails.firstOrNull?.url;
//
//        // Sometimes images hide in media:content
//        imageUrl = imageUrl ?? _searchForImageInMediaContent(item.media?.content);
//
//        // Sometimes they hide in media:group
//        imageUrl = imageUrl ?? _searchForImageInMediaGroup(item.media?.group);
//
//        // Fallback to extracting first <img src=""> tag from HTML content
//        imageUrl ??= item.content.expand((content) {
//            return parse(content.value)
//                .getElementsByTagName('img')
//                .map((element) => element.attributes['src'])
//            .nonNulls
//            .toList();
//        }).firstOrNull;
//
//        return imageUrl;
//    }
//
//    static String? _searchForImageInMediaContent(List<MediaContent>? content) {
//
//        // TODO: try to filter on type????
//
//        // Search for default
//        final foundDefault = content?.firstWhereOrNull((mc) {
//            return mc.isDefault == 'true';
//        });
//        if (foundDefault != null) {
//            return foundDefault.url;
//        }
//
//        // Search for biggest
//        final biggest = content?.fold<MediaContent?>(null, (acc, next) {
//            final oldWidth = int.parse(acc?.width ?? '-1');
//            final oldHeight = int.parse(acc?.height ?? '-1');
//            final newWidth = int.parse(next.width ?? '-1');
//            final newHeight = int.parse(next.height ?? '-1');
//            if (newWidth > oldWidth || newHeight > oldHeight) {
//                return next;
//            }
//            return acc;
//        });
//        if (biggest != null) {
//            return biggest.url;
//        }
//
//        // Take last
//        // why last? dunno, on theguardian com last <media:content> image is com biggest
//        return content?.lastOrNull?.url;
//    }
//
//    static String? _searchForImageInMediaGroup(List<Media>? mediaGroup) {
//
//        final medias = mediaGroup ?? [];
//
//        for (Media media in medias) {
//            // just grab first media content
//            final image = _searchForImageInMediaContent(media.content);
//            if (image != null) {
//                return image;
//            }
//        }
//
//        return null;
//    }
//}