import SwiftUI

public extension Color {
    static var dsPrimary: Color {
        .init(light: .init(hex: 0x2856ED), dark: .init(hex: 0xA491BB))
    }

    static var dsOnPrimary: Color {
        .init(light: .init(hex: 0xFFFFFF), dark: .init(hex: 0x150E1C))
    }

    static var dsPrimaryContainer: Color {
        .init(light: .init(hex: 0x8AABF5), dark: .init(hex: 0x533972))
    }

    static var dsOnPrimaryContainer: Color {
        .init(light: .init(hex: 0xFFFFFF), dark: .init(hex: 0xE1DAE8))
    }

    static var dsPrimaryVariant: Color {
        .init(light: .init(hex: 0x3E2B55), dark: .init(hex: 0xC3B5D2))
    }

    static var dsOnPrimaryVariant: Color {
        .init(light: .init(hex: 0xFFFFFF), dark: .init(hex: 0x150E1C))
    }

    static var dsSecondary: Color {
        .init(light: .init(hex: 0x02B09E), dark: .init(hex: 0x35C0B1))
    }

    static var dsTertiary: Color {
        .init(light: .init(hex: 0xB2675E), dark: .init(hex: 0xC1857E))
    }

    static var dsWarning: Color {
        .init(light: .init(hex: 0xEFA400), dark: .init(hex: 0xF2B633))
    }

    static var dsSuccess: Color {
        .init(light: .init(hex: 0x007D00), dark: .init(hex: 0x009C00))
    }

    static var dsError: Color {
        .init(light: .init(hex: 0xDC362E), dark: .init(hex: 0xE46962))
    }

    static var dsBackground: Color {
        .init(light: .init(hex: 0xF7F7F7), dark: .init(hex: 0x000000))
    }

    static var dsOnBackground: Color {
        .init(light: .init(hex: 0x000000), dark: .init(hex: 0xF7F7F7))
    }

    static var dsOnBackgroundSecondary: Color {
        .init(light: .init(hex: 0x575757), dark: .init(hex: 0x9A9A9A))
    }

    static var dsSurface: Color {
        .init(light: .init(hex: 0xFFFFFF), dark: .init(hex: 0x232323))
    }

    static var dsOnSurface: Color {
        .init(light: .init(hex: 0x000000), dark: .init(hex: 0xF7F7F7))
    }

    static var dsOnSurfaceSecondary: Color {
        .init(light: .init(hex: 0x575757), dark: .init(hex: 0x9A9A9A))
    }

    static var dsOutline: Color {
        .init(light: .init(hex: 0xDDDDDD), dark: .init(hex: 0x343434))
    }

    static var dsBackgroundVariant: Color {
        .init(light: .init(hex: 0xBCBCBC), dark: .init(hex: 0x464646))
    }

    static var dsOnBackgroundVariant: Color {
        .init(light: .init(hex: 0x797979), dark: .init(hex: 0x797979))
    }

    static var dsBackgroundVariant2: Color {
        .init(light: .init(hex: 0xF7F7F7), dark: .init(hex: 0x232323))
    }

    static var dsOnBackgroundVariant2: Color {
        .init(light: .init(hex: 0x797979), dark: .init(hex: 0x797979))
    }
}

public enum FormColors {
    public static let button: Color = .dsPrimary
    public static let pressedButton: Color = .dsPrimaryVariant
    public static let error: Color = .dsError
}

public extension Color {
    init(hex: UInt, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xFF) / 255,
            green: Double((hex >> 08) & 0xFF) / 255,
            blue: Double((hex >> 00) & 0xFF) / 255,
            opacity: alpha
        )
    }
}

struct ColorPaletteGrid: View {
    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading) {
                ColorsRow(colors: [
                    ("Primary", .dsPrimary),
                    ("On Primary", .dsOnPrimary)
                ])
                ColorsRow(colors: [
                    ("Primary Container", .dsPrimaryContainer),
                    ("On Primary Container", .dsOnPrimaryContainer)
                ])
                ColorCell(name: "Secondary", color: .dsSecondary)
                ColorCell(name: "Tertiary", color: .dsTertiary)
                ColorCell(name: "Warning", color: .dsWarning)
                ColorCell(name: "Success", color: .dsSuccess)
                ColorCell(name: "Error", color: .dsError)
                ColorsRow(colors: [
                    ("Background", .dsBackground),
                    ("On Background", .dsOnBackground),
                    ("On Background Secondary", .dsOnBackgroundSecondary)
                ])
                ColorsRow(colors: [
                    ("Surface", .dsSurface),
                    ("On Surface", .dsOnSurface),
                    ("On Surface Secondary", .dsOnSurfaceSecondary)
                ])
                ColorsRow(colors: [
                    ("Background Variant", .dsBackgroundVariant),
                    ("On Background Variant", .dsOnBackgroundVariant)
                ])
                ColorsRow(colors: [
                    ("Background Variant 2", .dsBackgroundVariant2),
                    ("On Background Variant 2", .dsOnBackgroundVariant2)
                ])
            }
            .padding()
        }
    }

    struct ColorsRow: View {
        let colors: [(String, Color)]
        var body: some View {
            HStack {
                ForEach(colors, id: \.0) { color in
                    ColorCell(name: color.0, color: color.1)
                }
            }
        }
    }

    struct ColorCell: View {
        let name: String
        let color: Color

        var body: some View {
            ZStack {
                color
                    .frame(height: 80)
                    .frame(maxWidth: 160)
                    .cornerRadius(8)
                    .shadow(radius: 2)

                Text(name)
                    .font(.caption.bold())
                    .foregroundColor(.white)
                    .shadow(color: .black, radius: 1)
            }
        }
    }
}

// Grid with al the colors
#Preview {
    HStack(spacing: 0) {
        ColorPaletteGrid()
            .background(Color.dsBackground)
            .colorScheme(.light)
        ColorPaletteGrid()
            .background(Color.dsBackground)
            .colorScheme(.dark)
    }
}
